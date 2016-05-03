package monitoring

import akka.actor.{Actor, ActorSystem, Props, Terminated}

class WatchActor extends Actor {
 //
  val child = context.actorOf(Props.empty, "child")
  context.watch(child)
  var lastSender = self
 
  def receive = {
    case "kill"              ⇒ {
      println("Watch actor: stopping child...")
      context.stop(child)
      lastSender = sender
    }
    case Terminated(corpse) ⇒
      if (corpse == child) {
        println("Watch actor: child terminated")
        lastSender ! "finished"
      }
  }
}

class Parent extends Actor {
 val watcher = context.actorOf(Props[WatchActor], "WatchActor")

  def receive = {
    case "finished" => println("Parent: done")
    case msg: AnyRef  => watcher ! msg
  }
}

object Monitoring extends App {

  val system = ActorSystem("MonitoringApp")
  val parent = system.actorOf(Props[Parent], "Parent")

  Thread.sleep(500)
  parent ! "kill"
  Thread.sleep(1000)
  system.terminate
}
