import akka.actor.{ActorSystem, Actor, Props, PoisonPill}
import akka.event.Logging
import akka.routing.{RoundRobinPool, Broadcast}

object SimpleRouter2 extends App {
  val system = ActorSystem("SimpleSystem")
  val simpleRouter = system.actorOf(Props[SimpleActor].withRouter(
                        RoundRobinPool(nrOfInstances = 5)
                     ), name = "simpleRoutedActor")
  simpleRouter ! Broadcast(Message("I will not buy this record, it is scratched!"))

  for (n <- 1 to 10)  simpleRouter ! Message("Hello, Akka #%d!".format(n))
  simpleRouter ! Broadcast(PoisonPill)
  simpleRouter ! Message("Hello? You're looking a little green around the gills...") // never gets read
}
