package token

import akka.actor.{Actor, ActorSystem, Props}

case class Init(nextToken1: String, nextToken2: String)
case object Token1
case object Token2

class Processor extends Actor {
  var state = (0, 0)
  var nextToken1=""
  var nextToken2=""
  
  def receive = {
    case Init(nextToken1, nextToken2) =>
      this.nextToken1 = nextToken1
      this.nextToken2 = nextToken2
    case Token1 => 
      state = (state._1 + 1, state._2)
      println(self.path.name + " got Token1 from " + sender.path.name + ", new state: (" + state._1 + ", " + state._2 + ")")
      Thread.sleep(500)
      val receiver = context.actorSelection(nextToken1)
      receiver ! Token1
    case Token2 => 
      state = (state._1, state._2 + 1)
      println(self.path.name + " got Token2 from " + sender.path.name + ", new state: (" + state._1 + ", " + state._2 + ")")
      Thread.sleep(600)
      val receiver = context.actorSelection(nextToken2)
      receiver ! Token2
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
}