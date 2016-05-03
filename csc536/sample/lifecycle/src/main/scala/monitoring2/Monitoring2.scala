package monitoring2

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}

class WatchActor (peerActor: ActorRef) extends Actor {
  //
  context.watch(peerActor)
 
  def receive = {
    case Terminated(corpse) ⇒  
      if (corpse == peerActor) {
        println("Sibling watch actor: child terminated, informing parent")
        context.parent ! "finished"
    }
  }
}

class Parent extends Actor {
  val child = context.actorOf(Props.empty, "Child")
  val watcher = context.actorOf(Props(new WatchActor(child)), "WatchActor")

  def receive = {
    case "kill"       ⇒ {
      println("Parent: stopping child...")
      context.stop(child)
    }
    case "finished"   => println("Parent: done")
  }
}

object Monitoring2 extends App {

  val system = ActorSystem("MonitoringApp")
  val parent = system.actorOf(Props[Parent], "Parent")

  Thread.sleep(500)
  parent ! "kill"
  Thread.sleep(1000)
  system.terminate
}

