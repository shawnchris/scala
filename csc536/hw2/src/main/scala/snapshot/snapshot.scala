package snapshot

import akka.actor.{Actor, ActorSystem, Props}

case class Init(nextToken1: String, nextToken2: String)
case object Token1
case object Token2
case object Marker

class Processor extends Actor {
  var state = (0, 0)
  var nextToken1 = ""
  var nextToken2 = ""
  var snapshotState = (0, 0)
  var snapshotStarted = false;
  var recordChannel1 = true;
  var recordChannel2 = true;
  var sentChannel1 = false;
  var sentChannel2 = false;
  var inTransit = "";
  
  def receive = {
    case Init(nextToken1, nextToken2) =>
      this.nextToken1 = nextToken1
      this.nextToken2 = nextToken2
    case Token1 => 
      state = (state._1 + 1, state._2)
      println(self.path.name + " got Token1 from " + sender.path.name + ", new state: (" + state._1 + ", " + state._2 + ")")
      // record in-transit message. Token1 arrives from channel2.
      if (snapshotStarted && recordChannel2) {
        inTransit += "[Token1 from " + sender.path.name + "]"
        println(self.path.name + " recorded in-transit message: Token1 from " + sender.path.name)
      }
      Thread.sleep(500)
      val receiver = context.actorSelection(nextToken1)
      receiver ! Token1
    case Token2 => 
      state = (state._1, state._2 + 1)
      println(self.path.name + " got Token2 from " + sender.path.name + ", new state: (" + state._1 + ", " + state._2 + ")")
      // record in-transit message. Token2 arrives from channel1.
      if (snapshotStarted && recordChannel1) {
        inTransit += "[Token2 from " + sender.path.name + "]"
        println(self.path.name + " recorded in-transit message: Token2 from " + sender.path.name)
      }
      Thread.sleep(600)
      val receiver = context.actorSelection(nextToken2)
      receiver ! Token2
    case Marker =>
      // records its current process state
      println(self.path.name + " got Marker from " + sender.path.name)
      if (!snapshotStarted) {
        snapshotStarted = true;
        snapshotState = (state._1, state._2)
      }
      // set marker incoming channel as the empty set;
      // turn on recording of messages arriving over other incoming channels
      if (sender.path.toString() == nextToken1) {
        recordChannel1 = false
      }
      if (sender.path.toString() == nextToken2) {
        recordChannel2 = false
      }
      // send one marker message over outgoing channel if haven't done so before.
      // avoid keep sending markers
      if (!sentChannel1) {
        sentChannel1 = true
        val receiver = context.actorSelection(nextToken1)
        receiver ! Marker
      }
      if (!sentChannel2) {
        sentChannel2 = true
        val receiver = context.actorSelection(nextToken2)
        receiver ! Marker
      }
      // once all incoming channels are closed, snapshot is done.
      if (!recordChannel1 && !recordChannel2) {
        println(self.path.name + " snapshot is done. state: (" + snapshotState._1 + ", " + snapshotState._2 + "). In-transit message: " + inTransit)
      }
  }
}

object Server extends App {
  val system = ActorSystem("TokenRing")
  val actor1 = system.actorOf(Props[Processor], name = "Actor1")
  val actor2 = system.actorOf(Props[Processor], name = "Actor2")
  val actor3 = system.actorOf(Props[Processor], name = "Actor3")
  println(actor1.path);
  println(actor2.path);
  println(actor3.path);
  actor1 ! Init(actor2.path.toString(), actor3.path.toString())
  actor2 ! Init(actor3.path.toString(), actor1.path.toString())
  actor3 ! Init(actor1.path.toString(), actor2.path.toString())
  actor1 ! Token1
  actor1 ! Token2
  Thread.sleep(5000)
  actor1 ! Marker
}