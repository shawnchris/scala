import akka.actor.{Actor, ActorSystem, Props}

class FirstS extends Actor {
  def receive = {
    case "hello" => println("Hello world!")
    case msg: String => println("Got " + msg)
    case _ => println("Unknown message")
  }
}

object FirstAppS {
  def main(args : Array[String]) : Unit = {

    val system = ActorSystem("FirstExample")

    val first = system.actorOf(Props[FirstS], name = "first")

    println("The path associated with first is " + first.path)

    first ! "hello"
    first ! "Goodbye"
    first ! 4

    system.terminate
  }
}
