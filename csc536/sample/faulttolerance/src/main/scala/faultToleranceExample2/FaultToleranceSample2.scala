package FaultToleranceExample2

import akka.actor.{Actor, ActorSystem, Props, OneForOneStrategy}
import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}

class Child extends Actor {

  var sum = 0
  Thread.sleep(10)
  System.out.println("Child created with sum = " + sum)

  def receive = {
    case value: Int => {
      sum += value
      System.out.println("Sum = " + sum)
      val div = 1/value         // if value is 0, throw new ArithmeticException
    }
    case "stateError" => throw new IllegalStateException("State problems")
    case _            => throw new IllegalArgumentException("Expect only integers")
  }
}



class Supervisor extends Actor {

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalArgumentException => Resume
    case _: ArithmeticException      => Stop
    case _: Exception                => Restart
  }

  val child0 = context.actorOf(Props[Child])
  val child1 = context.actorOf(Props[Child])
  var turn = 0

  def receive = {
    case any: AnyRef => {
      if (turn == 0)
        child0 ! any
      else
        child1 ! any
      turn = 1-turn
    }
  }
}

object FaultToleranceSample extends App {
  val system = ActorSystem("FaultToleranceSample")
  val supervisor = system.actorOf(Props[Supervisor])
  supervisor ! 4
  supervisor ! "anything"
  supervisor ! 2
  supervisor ! "stateError"
  supervisor ! 3
  supervisor ! 0
  supervisor ! 5 

  Thread.sleep(500)
  system.terminate
}
