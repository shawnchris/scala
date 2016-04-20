package server

import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import share._

object MapReduceApplication extends App {

  val system = ActorSystem("MapReduceApp",ConfigFactory.load.getConfig("server"))
  val master = system.actorOf(Props[MasterActor], name = "master")
  println("System complete")
  
}
