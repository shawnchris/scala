package client

import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorRef, ActorLogging, ActorSystem, Props, AddressFromURIString}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool
import share._

class ClientActor extends Actor{
  val master = context.actorSelection("akka.tcp://MapReduceApp@127.0.0.1:2552/user/master")
  var numberRemoteMappers = ConfigFactory.load.getInt("number-remoteMappers")
  var numberRemoteReducers = ConfigFactory.load.getInt("number-remoteReducers")
  var reduceActors=List[ActorRef]()
  for(i <- 0 until numberRemoteReducers)
  {
    reduceActors = context.actorOf(Props[ReduceActor], name="remotereduce"+i)::reduceActors
  }
  var mapActors=List[ActorRef]()

  master ! AddReducer(reduceActors, self)

  def receive = {
    case AddMapper(reducers:List[ActorRef])=>
      for(i <- 0 until numberRemoteMappers)
      {
        mapActors = context.actorOf(Props(classOf[MapActor], reducers), name="remotemapper"+i)::mapActors
      }
	
      sender ! AddMapper(mapActors)
    case Done =>
      println("Receive Done")
      master ! Done
  }


}
