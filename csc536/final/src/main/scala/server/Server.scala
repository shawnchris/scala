package server
import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, Props, ActorLogging}
import common._

object Server extends App {
    val system = ActorSystem("MapReduceSystem", ConfigFactory.load.getConfig("server"))
    println("Server ready")
}
