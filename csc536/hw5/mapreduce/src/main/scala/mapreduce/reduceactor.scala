package mapreduce

import scala.collection.mutable.HashMap

import akka.actor.{ Actor, ActorRef }
import com.typesafe.config.ConfigFactory

class ReduceActor extends Actor {
  var remainingMappers = 3
  var reduceMap = HashMap[String, List[String]]()

  def receive = {
    case Pair(name: String, title: String) =>
      if (reduceMap.contains(name)) {
        reduceMap += (name -> (title :: reduceMap(name)))
      } else {
        reduceMap += (name -> (title :: Nil))
      }
    case Flush =>
      remainingMappers -= 1
      if (remainingMappers == 0) {
        println(self.path.name + " work is done. To make the output clear, I won't print reduce results.")
        //for ((key, value) <- reduceMap)
        //	println(key + " --> " + value)
        context.parent ! Done
      }
  }
}
