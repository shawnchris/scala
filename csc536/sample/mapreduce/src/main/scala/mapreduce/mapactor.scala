package mapreduce

import akka.actor.{Actor, ActorRef}

class MapActor(reduceActors: List[ActorRef]) extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
  val numReducers = reduceActors.size

  def receive = {
    case msg: String =>
      process(msg)
    case Flush => 
      for (i <- 0 until numReducers) {
        reduceActors(i) ! Flush
      }
  }

  def process(content: String) = {
    for (word <- content.toLowerCase.split("[\\p{Punct}\\s]+")) 
      if ((!STOP_WORDS_LIST.contains(word))) {
        var index = Math.abs((word.hashCode())%numReducers)
	reduceActors(index) ! Word(word, 1)
      }
  }
}
