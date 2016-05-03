package mapreduce

import akka.actor.{ Actor, ActorRef, Props, OneForOneStrategy }
import akka.routing.{ Broadcast, RoundRobinPool }
import akka.actor.SupervisorStrategy.{ Restart, Resume, Stop }

class MasterActor extends Actor {

  val numberMappers = 3
  val numberReducers = 3
  var pending = numberReducers

  var reduceActors = List[ActorRef]()
  for (i <- 0 until numberReducers)
    reduceActors = context.actorOf(Props[ReduceActor], name = "reduce" + i) :: reduceActors

  val mapActors = context.actorOf(RoundRobinPool(numberMappers).props(Props(classOf[MapActor], reduceActors)))

  def receive = {
    case Message(title: String, url: String) =>
      mapActors ! Message(title, url)
    case RetryMessage(title: String, url: String) =>
      sender ! RetryMessage(title, url)
    case Flush =>
      mapActors ! Broadcast(Flush)
    case Done =>
      pending -= 1
      if (pending == 0)
        context.system.terminate
  }

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalStateException    => Restart
    case _: IllegalArgumentException => Resume
    case _: Exception                => Stop
  }
}
