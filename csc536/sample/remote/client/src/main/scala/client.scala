import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, ActorLogging}


object Client extends App {
  val system = ActorSystem("GreetingSystem", ConfigFactory.load.getConfig("remotelookup"))
  val joe = system.actorSelection("akka.tcp://GreetingSystem@127.0.0.1:2552/user/joe")

  joe ! "a hello from a remote client!"
  println("Client has sent Hello to joe")
}
