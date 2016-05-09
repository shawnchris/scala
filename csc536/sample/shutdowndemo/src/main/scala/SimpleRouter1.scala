import akka.actor.{ActorSystem, Actor, Props}
import akka.event.Logging
import akka.routing.RoundRobinPool


case class Message(msg: String)


class SimpleActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case Message(msg) => log.info("Got a valid message: %s".format(msg))
    case default => log.error("Got a message I don't understand.")
  }
}


object SimpleRouter1 extends App {
  val system = ActorSystem("SimpleSystem")
  val simpleRouted = system.actorOf(Props[SimpleActor].withRouter(
                        RoundRobinPool(nrOfInstances = 5)
                     ), name = "simpleRoutedActor")

  for (n <- 1 to 10)  simpleRouted ! Message("Hello, Akka #%d!".format(n))
}
