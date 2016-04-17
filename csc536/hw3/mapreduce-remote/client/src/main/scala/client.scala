import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem, Props, AddressFromURIString, ActorLogging}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool

object Client extends App {
  val system = ActorSystem("MapReduceSystem", ConfigFactory.load.getConfig("remotelookup"))
  val localServer = ConfigFactory.load.getString("local-server-address")
  val remoteServer = ConfigFactory.load.getString("remote-server-address")
  val localMaster = system.actorSelection(localServer + "/user/master")
  val remoteMaster = system.actorSelection(remoteServer + "/user/master")

  localMaster ! Message("A Tale of Two Cities","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg98.txt")
  localMaster ! Message("The Cricket on the Hearth","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt")
  remoteMaster ! Message("Oliver Twist","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg730.txt")
  remoteMaster ! Message("Hunted Down","http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg807.txt")
  /*
  localMaster ! Message("Part 4", "http://www.fullbooks.com/Co-Aytch-1.html")
  localMaster ! Message("Part 5", "http://www.fullbooks.com/Co-Aytch-2.html")
  localMaster ! Message("Part 6", "http://www.fullbooks.com/Co-Aytch-3.html")
  localMaster ! Message("Part 7", "http://www.fullbooks.com/Co-Aytch-4.html")
  localMaster ! Message("Part 8", "http://www.fullbooks.com/Co-Aytch-5.html")
  */
  println("Client has sent mapreduce jobs to MasterActors")
  localMaster ! Flush
  remoteMaster ! Flush
  Thread.sleep(3000)
  system.terminate
}
