import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, ActorLogging, Props, Identify, ActorIdentity, ActorRef, Terminated}

class WatcherActor extends Actor {
  val watcheeactor = context.actorSelection("akka.tcp://WatcheeSystem@127.0.0.1:2552/user/watcheeactor")
  watcheeactor ! Identify(1)
  
  def receive = {
	case ActorIdentity(1, Some(ref)) =>
      context.watch(ref)
      context.become(active(ref))
	  println("WatcherActor is ready.")
    case ActorIdentity(1, None) => context.stop(self)
    case msg: String => println("WatcherActor received " + msg)
    case _ => println("WatcherActor received unknown msg ")
  }
  
  def active(another: ActorRef): Actor.Receive = {
    case Terminated(another) =>
	  println("\n!!!!!!!!!!WatcheeActor terminated.!!!!!!!!!!\n")
	  context.system.terminate
  }
}

object Watcher extends App {
  val system = ActorSystem("WatcherSystem", ConfigFactory.load.getConfig("watcher"))
  val watcheractor = system.actorOf(Props[WatcherActor], name = "watcheractor")
}
