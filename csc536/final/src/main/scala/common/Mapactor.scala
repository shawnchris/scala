package common

import akka.actor.{Actor, ActorRef}

class MapActor extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
  var numReducers = 0
  var arrayReducers: Array[ActorRef] = null
  var mapfunc: MapTrait = null

  def receive = {
    case SetReducers(reducers: Array[ActorRef]) => {
      arrayReducers = reducers
      numReducers = arrayReducers.length
      println("MapActor received ReduceActors:")
      for (i <- 0 until numReducers) {
        println(arrayReducers(i))
      }
    }
    // Set customized map function
    case f: MapTrait => {
      mapfunc = f
    }
    case msg: String => {
      println("MapActor received: " + msg)
      // Process it using customized map function
      // And send results to different reducers according to hash % size.
      for (p: Pair <- mapfunc.process(msg)) {
        val hash = Math.abs(p.key.hashCode())
        arrayReducers(hash % arrayReducers.size) ! p
      }
    }
    case Flush => {
      for (i <- 0 until numReducers) {
        arrayReducers(i) ! Flush
      }
      println("All jobs are done, MapActor is going to exit...")
      context.stop(self)
    }
    case _ => {
      println("MapActor received an invalid message.")
    }
  }
}
