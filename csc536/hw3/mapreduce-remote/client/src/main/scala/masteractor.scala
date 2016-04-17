import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{Broadcast, RoundRobinPool}
import com.typesafe.config.ConfigFactory

class MasterActor extends Actor {

  val numberMappers  = ConfigFactory.load.getInt("number-mappers")
  val numberReducers  = ConfigFactory.load.getInt("number-reducers")
  println("number-mappers: " + numberMappers)
  println("number-reducers: " + numberReducers)
  var pending = numberReducers

  var reduceActors = List[ActorRef]()
  for (i <- 0 until numberReducers)
    reduceActors = context.actorOf(Props[ReduceActor], name = "reduce" + i)::reduceActors
  println("reduceActors: " + reduceActors)

  val mapActors = context.actorOf(RoundRobinPool(numberMappers).props(Props(classOf[MapActor], reduceActors)))

  def receive = {
    case Message(title:String,url:String) =>
      mapActors ! Message(title,url)
	  println("MasterActor send: " + title + " " + url)
    case Flush =>
      mapActors ! Broadcast(Flush)
    case Done =>
      pending -= 1
      if (pending == 0)
        context.system.terminate
  }
}
