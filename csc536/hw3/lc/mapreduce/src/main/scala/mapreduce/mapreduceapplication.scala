package mapreduce

import akka.actor.{ActorSystem, Props}

object MapReduceApplication extends App {

  val system = ActorSystem("MapReduceApp")
  val master = system.actorOf(Props[MasterActor], name = "master")

  master ! Message("Bleak House","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1023.txt")
  master ! Message("Great Expectations","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg1400.txt")
  master ! Message("A Christmas Carol","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg19337.txt")
  master ! Message("The Cricket on the Hearth","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
  master ! Flush
}
