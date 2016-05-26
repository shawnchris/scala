package common

import scala.collection.mutable.HashMap
import akka.actor.{Actor, ActorRef}

class ReduceActor extends Actor {
  var remainingMappers = 0
  var reduceMap = HashMap[String, String]()
  var masterRef: ActorRef = null
  var reducefunc: ReduceTrait = null

  def receive = {
    case SetNumMapper(num: Int) => {
      remainingMappers = num
    }
    case SetMaster(master: ActorRef) => {
      masterRef = master
    }
    // Set customized reduce function
    case f: ReduceTrait => {
      reducefunc = f
    }
    case p: Pair => {
      reducefunc.process(p, reduceMap)
    }
    case Flush => {
      remainingMappers -= 1
      if (remainingMappers == 0) {
        println(reduceMap)
        println("All jobs are done, ReduceActor is going to exit...")
        masterRef ! Done
        context.stop(self)
      }
    }
    case _ => {
      println("ReduceActor received an invalid message.")
    }
  }
}
