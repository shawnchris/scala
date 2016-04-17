import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, Props}

object Server extends App {
  val system = ActorSystem("MapReduceSystem", ConfigFactory.load.getConfig("server"))
  val master = system.actorOf(Props[MasterActor], name = "master")
  println("Server ready")
  //Thread.sleep(30000)
  //system.terminate
}
