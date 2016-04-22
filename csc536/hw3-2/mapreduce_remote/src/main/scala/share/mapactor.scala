package share 

import akka.actor.{Actor, ActorRef}
import scala.io.Source
import scala.collection.mutable.HashMap

class MapActor(reduceActors: List[ActorRef]) extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to", "this")
  val numReducers = reduceActors.size
  val hashmap=HashMap[String,String]()

  def receive = {
    case Message(title:String,url:String) =>
      println("Get "+title)
      process(title,url)
    case Flush => 
      for (i <- 0 until numReducers) {
        reduceActors(i) ! Flush
      }
  }

  def process(title: String,url: String) = {
    val content= Source.fromURL(url).mkString
    for (name <- content.split("[\\p{Punct}\\s]+")) 
      if ((!STOP_WORDS_LIST.contains(name.toLowerCase))&&name.matches("\\p{Upper}[\\p{Lower}]*")) {
        if(!(hashmap.contains(name)))
	{ 
	  hashmap.put(name,url)
          var index = Math.abs((name.hashCode())%numReducers)
	  reduceActors(index) ! Pair(name, title)
	}
      }
  }
}
