package mapreduce

import scala.collection.mutable.HashMap

import akka.actor.{Actor, ActorRef}
import com.typesafe.config.ConfigFactory

class ReduceActor extends Actor {
  var remainingMappers = ConfigFactory.load.getInt("number-mappers")
  var reduceMap = HashMap[String, Int]()

  def receive = {
    case Word(word, count) =>
      if (reduceMap.contains(word)) {
	reduceMap += (word -> (reduceMap(word) + 1))
      }
      else
	reduceMap += (word -> 1)
    case Flush =>
      remainingMappers -= 1
      if (remainingMappers == 0) {
        println(self.path.toStringWithoutAddress + " : " + reduceMap)
        context.parent ! Done
      }
  }
}
