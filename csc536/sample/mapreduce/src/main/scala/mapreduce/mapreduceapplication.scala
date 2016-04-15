package mapreduce

import akka.actor.{ActorSystem, Props}

object MapReduceApplication extends App {

  val system = ActorSystem("MapReduceApp")
  val master = system.actorOf(Props[MasterActor], name = "master")

  master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
  master ! "Dog is man's best friend"
  master ! "Dog and Fox belong to the same family"
  master ! Flush
}
