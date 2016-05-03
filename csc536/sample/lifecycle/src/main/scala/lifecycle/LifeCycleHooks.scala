package lifecycle

import akka.actor.{Actor, ActorSystem, Props, OneForOneStrategy, ActorLogging}
import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}

class LifeCycleHooks extends Actor with ActorLogging {

  override def preStart() {println("preStart")}

  override def postStop() {println("postStop")}

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) {
    println("postRestart")
    super.postRestart(reason)
  }

  println("Constructor")

  def receive = {
    case "restart" =>
      println("Received restart")
      throw new IllegalStateException("force restart")
    case "resume" =>
      println("Received resume")
      throw new IllegalArgumentException("force resume")
    case msg: AnyRef =>
      println("Received any")
  }
}

class Parent extends Actor with ActorLogging {

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalStateException    => Restart
    case _: IllegalArgumentException => Resume
    case _: Exception                => Stop
  }

  val child = context.actorOf(Props[LifeCycleHooks])

  def receive = {
    case any: AnyRef => child ! any
  }
}


object LifeCycleSample extends App {

  val system = ActorSystem("LifeCycleSample")
  val parent = system.actorOf(Props[Parent], "Parent")

  parent ! "message" 
  Thread.sleep(500)
  parent ! "restart"
  Thread.sleep(500)
  parent ! "resume"
  Thread.sleep(500)
  parent ! "message"
 
  system.stop(parent)

  Thread.sleep(5000)
  system.terminate
}
