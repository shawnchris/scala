package mapreduce

import akka.actor.{ActorSystem, Props}

object MapReduceApplication extends App {

  val system = ActorSystem("MapReduceApp")
  val master = system.actorOf(Props[MasterActor], name = "master")
  
  master ! Message("Does Not Exist","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/doesnotexist.txt")
  master ! Message("Hunted Down","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg807.txt")
  master ! Message("A Tale of Two Cities","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg98.txt")
  master ! Message("The Cricket on the Hearth","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
  master ! Message("Oliver Twist","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg730.txt")
  master ! Flush
}
