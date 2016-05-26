package common

import akka.actor.{Actor, ActorRef}

class MasterActor extends Actor {

  var numberMappers = 0
  var numberReducers = 0
  var arrayMappers: Array[ActorRef] = null
  var seq = 0
  var pending = 0

  def receive = {
    case SetMappers(mappers: Array[ActorRef]) => {
      arrayMappers = mappers
      numberMappers = arrayMappers.length
      
      println("MasterActor received MapActors:")
      for (i <- 0 until numberMappers) {
        println(arrayMappers(i))
      }
    }
    case SetNumReducer(num: Int) => {
      numberReducers = num
      pending = num
    }
    case msg: String => {
      arrayMappers(seq % numberMappers) ! msg
      seq += 1
    }
    case Flush => {
      for (i <- 0 until numberMappers) {
        arrayMappers(i) ! Flush
      }
    }
    case Done => {
      pending -= 1
      if (pending == 0) {
        println("All jobs are done, MasterActor is going to exit...")
        context.stop(self)
      }
    }
  }
}
