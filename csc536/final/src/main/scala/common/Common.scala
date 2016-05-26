package common
import akka.actor.Actor
import scala.collection.mutable.HashMap

@SerialVersionUID(100L)
abstract class MapTrait extends Serializable {
  def process(url: String): List[Pair]
}

abstract class ReduceTrait extends Serializable {
  def process(p: Pair, m: HashMap[String, String])
}

class Joe extends Actor {
  def receive = {
    case msg: String => println("joe received " + msg)
    case mapper: MapTrait => {
      println(mapper.process(""))
    }
    case _ => println("Received unknown msg ")
  }
}