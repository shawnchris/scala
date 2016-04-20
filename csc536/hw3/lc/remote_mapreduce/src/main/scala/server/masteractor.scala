package server

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{Broadcast, RoundRobinPool}
import com.typesafe.config.ConfigFactory
import akka.remote.routing.RemoteRouterConfig
import share._

class MasterActor extends Actor {

  var numberMappers  = ConfigFactory.load.getInt("number-mappers")
  var numberReducers  = ConfigFactory.load.getInt("number-reducers")
  var numberRemoteMappers  = ConfigFactory.load.getInt("number-remoteMappers")
  var numberRemoteReducers  = ConfigFactory.load.getInt("number-remoteReducers")
  var pending = numberReducers

  var reduceActors = List[ActorRef]()
  for (i <- 0 until numberReducers)
    reduceActors = context.actorOf(Props[ReduceActor], name = "reduce"+i)::reduceActors
  var mapActors = List[ActorRef]()
  //val mapActors = context.actorOf(RoundRobinPool(numberMappers).props(Props(classOf[MapActor], reduceActors)))
  
  var actorBroadCast=context.actorOf(Props.empty)
 
  //mapBroadCast= context.actorOf(RemoteRouterConfig(RoundRobinPool(numberMappers), temp).props(Props(classOf[MapActor], reduceActors)))
  //self ! Start
  var clients = List[ActorRef]()
  def receive = {
    //add remote mapper
    case AddMapper(actors:List[ActorRef]) =>
      for(actor <- actors)
      {
        mapActors = actor :: mapActors
        numberMappers = numberMappers + 1
      }
      var seq = mapActors.map(x=> x.path.address).seq
      actorBroadCast = context.actorOf(RemoteRouterConfig(RoundRobinPool(numberMappers), seq).props(Props(classOf[MapActor], reduceActors)))
      self ! Start
    //case GetReducer(actors:List[ActorRef])=>
    //  println("Get GetMapper")
    //  sender ! GetReducer(reduceActors)
    //add remote reducer
    //to run the application we should add the reducer first
    case AddReducer(actors:List[ActorRef],client:ActorRef) =>
      for(actor <- actors){
        reduceActors = actor :: reduceActors
      }
      pending = pending + actors.size
      //update the map
      for(i <- 0 until numberMappers){
        mapActors = context.actorOf(Props(classOf[MapActor], reduceActors), name="map"+i)::mapActors
    }
      
      clients = client :: clients
      sender ! AddMapper(reduceActors)
    case Start =>
      self ! Message("Bleak House","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1023.txt")
      self ! Message("Great Expectations","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
      self ! Message("A Christmas Carol","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg19337.txt")
      self ! Message("The Cricket on the Hearth","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
      self ! Flush
    case Flush =>
      actorBroadCast ! Broadcast(Flush)
    case Message(title:String,url:String)=>
      actorBroadCast ! Message(title,url)
    case Done =>
      pending -= 1
      if (pending == 0)
      {
        for(client <- clients)
		client !Done
      
        Thread.sleep(10000)
        context.system.shutdown
      }
  }
}
