package client

import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, Props, AddressFromURIString, ActorLogging}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool


object Client extends App {
  val system = ActorSystem("MapreduceSystem", ConfigFactory.load.getConfig("remotelookup"))
  val client = system.actorOf(Props[ClientActor], name="client")
  println("Remote map client complete")


}
