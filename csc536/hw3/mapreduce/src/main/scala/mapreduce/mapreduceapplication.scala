package mapreduce

import akka.actor.{ActorSystem, Props}

object MapReduceApplication extends App {

  val system = ActorSystem("MapReduceApp")
  val master = system.actorOf(Props[MasterActor], name = "master")

  master ! Message("Hunted Down","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg807.txt")
  master ! Message("A Tale of Two Cities","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg98.txt")
  master ! Message("The Cricket on the Hearth","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
  master ! Message("Oliver Twist","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg730.txt")
  /*
  master ! Message("Part 1", "http://www.fullbooks.com/An-Attic-Philosopher-entire1.html")
  master ! Message("Part 2", "http://www.fullbooks.com/An-Attic-Philosopher-entire2.html")
  master ! Message("Part 3", "http://www.fullbooks.com/An-Attic-Philosopher-entire3.html")
  master ! Message("Part 4", "http://www.fullbooks.com/Co-Aytch-1.html")
  master ! Message("Part 5", "http://www.fullbooks.com/Co-Aytch-2.html")
  master ! Message("Part 6", "http://www.fullbooks.com/Co-Aytch-3.html")
  master ! Message("Part 7", "http://www.fullbooks.com/Co-Aytch-4.html")
  master ! Message("Part 8", "http://www.fullbooks.com/Co-Aytch-5.html")
  */
  master ! Flush
}
