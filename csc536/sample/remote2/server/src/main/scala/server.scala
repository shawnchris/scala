import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem}

class Joe extends Actor {
  def receive = {
    case msg: String => println(self.path + " received " + msg + " from " + sender)
    case _ => println("Received unknown msg ")
  }
}

object Server extends App {
  val system = ActorSystem("GreetingSystem", ConfigFactory.load.getConfig("server"))
  println("Server ready")
  Thread.sleep(30000)
  system.terminate
}
