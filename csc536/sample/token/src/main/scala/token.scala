import scala.collection.mutable.{Queue, Map}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

case class Neighbors(left:ActorRef, right:ActorRef)
case object PING // goes clockwise
case object PONG // goes counterclockwise

// SNAPSHOT: token classes
sealed trait Token
case object Marker extends Token
case object Start extends Token

class PingPong extends Actor {
  var left = self
  var right = self
  var PINGcount = 0
  var PONGcount = 0

  // SNAPSHOT: data structures
  var snapshot = Array(0, 0)
  var inSnapshot = false
  var recording = Map[ActorRef, Queue[Any]]()
  var closed = Map[ActorRef, Boolean]()
  var numOpen = 2

  def receive = {

    case PING =>
      PINGcount = PINGcount + 1
      printf("%12s : PING=%d, PONG=%d\n", self.path.toStringWithoutAddress, PINGcount, PONGcount)

      // SNAPSHOT: record message in transit
      if (inSnapshot && !closed(sender)) {
        recording(sender).enqueue(PING)
        printf("%12s : MESSAGE RECORDING\n%26s ---> %s\n", self.path.toStringWithoutAddress, sender.path.toStringWithoutAddress, PING)
      }

      Thread sleep 2
      left ! PING

    case PONG =>
      PONGcount = PONGcount + 1
      printf("%12s : PING=%d, PONG=%d\n", self.path.toStringWithoutAddress, PINGcount, PONGcount)

      // SNAPSHOT: record message in transit
      if (inSnapshot && !closed(sender)) {
        recording(sender).enqueue(PONG)
        printf("%12s : MESSAGE RECORDING\n%26s ---> %s\n", self.path.toStringWithoutAddress, sender.path.toStringWithoutAddress, PONG)
      }

      Thread sleep 2
      right ! PONG

    // SNAPSHOT: snapshot message
    case token : Token => 
      if (inSnapshot) {
      // SNAPSHOT: stop recording from sender + print state if done
        closed(sender) = true
        numOpen = numOpen - 1
        if (numOpen == 0) {
          printf("%12s : SNAPSHOT\n               STATE: %d %d\n               MESSAGES IN TRANSIT:\n               %s--->%s\n               %s--->%s\n", self.path.toStringWithoutAddress, snapshot(0), snapshot(1), left.path.toStringWithoutAddress, recording(left), right.path.toStringWithoutAddress, recording(right))
          inSnapshot = false
        }
      } else { 
        // SNAPSHOT: start snapshot
        printf("%12s : STARTING SNAPSHOT\n               STATE: %d %d\n", self.path.toStringWithoutAddress, PINGcount, PONGcount)
        inSnapshot = true
        snapshot(0) = PINGcount
        snapshot(1) = PONGcount
        recording(left) = Queue[Any]()
        recording(right) = Queue[Any]()
        closed(left) = false
        closed(right) = false
        if (token == Marker) {
          closed(sender) = true
          numOpen = 1
        } else {
          numOpen = 2
        }
        printf("%12s : SENDING MARKER TO NEIGHBORS\n", self.path.toStringWithoutAddress)
        left ! Marker
        right ! Marker
      }
      
    case Neighbors(l, r) =>
      left = l
      right = r
  }
}

object Server extends App {
  val system = ActorSystem("PingPong")
  val first = system.actorOf(Props[PingPong], name = "first")
  val second = system.actorOf(Props[PingPong], name = "second")
  val third = system.actorOf(Props[PingPong], name = "third")
  first ! Neighbors(second, third)
  second ! Neighbors(third, first)
  third ! Neighbors(first, second)
  first ! PING
  first ! PONG
  Thread.sleep(50)
  first ! Start
  Thread.sleep(20)
  second ! Start
  Thread.sleep(20)
  system.terminate
}
