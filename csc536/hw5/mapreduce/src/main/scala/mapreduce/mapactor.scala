package mapreduce

import akka.actor.{Actor, ActorRef}
import scala.io.Source
import scala.collection.mutable.HashMap

class MapActor(reduceActors: List[ActorRef]) extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
    "do", "go", "if", "in", "is", "it", "of", "on", "the", "to", "this")
  val numReducers = reduceActors.size
  val hashmap = HashMap[String, String]()
  var errFlag = false;
  var retryMsg: RetryMessage = null;
  
  override def preStart() {
    //println("MapActor " + self.path + " preStart")
  }

  override def postStop() {
    //println("MapActor " + self.path + " postStop")
    if (errFlag) {
      errFlag = false;
      context.parent ! retryMsg
    }
  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    //println("MapActor " + self.path + " preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) {
    //println("MapActor " + self.path + " postRestart")
    super.postRestart(reason)
  }

  def receive = {
    case Message(title: String, url: String) =>
      process(title, url, false)
    case RetryMessage(title: String, url: String) =>
      process(title, url, true)
    case Flush => 
      for (i <- 0 until numReducers) {
        reduceActors(i) ! Flush
      }
  }

	def process(title: String, url: String, retry: Boolean) = {
	  try {
		  val content = Source.fromURL(url).mkString
		  for (name <- content.split("[\\p{Punct}\\s]+")) {
  			if ((!STOP_WORDS_LIST.contains(name.toLowerCase)) && name.matches("\\p{Upper}[\\p{Lower}]*")) {
  				if(!(hashmap.contains(name))) {
  					hashmap.put(name,url)
  					var index = Math.abs((name.hashCode()) % numReducers)
  					reduceActors(index) ! Pair(name, title)
  				}
  			}
  		}
	  }
	  catch {
	    case _: Throwable => 
	      if (!retry) {
	        errFlag = true;
	        retryMsg = RetryMessage(title, url);
	        println("MapActor " + self.path + " first time can't process url: " + url)
	        throw new IllegalStateException("Resart to try this url again.")
	      }
	      else {
	        println("MapActor " + self.path + " second time can't process url: " + url)
	        throw new IllegalArgumentException("Give up, resume to other urls.")
	      }
	  }
		
	}
}
