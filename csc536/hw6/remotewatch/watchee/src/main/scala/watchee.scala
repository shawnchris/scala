import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, Props, ActorLogging, Identify, ActorIdentity}

class WatcheeActor extends Actor {
  def receive = {
    case msg: String => println("WatcheeActor received " + msg)
    case _ => println("WatcheeActor received unknown msg ")
  }
}

object Watchee extends App {
    val system = ActorSystem("WatcheeSystem", ConfigFactory.load.getConfig("watchee"))
    val watcheeactor = system.actorOf(Props[WatcheeActor], name = "watcheeactor")
    println("WatcheeActor is ready. Terminate me after WatcherActor is ready.")
}
