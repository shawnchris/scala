package client
import com.typesafe.config.ConfigFactory
import akka.actor._
import akka.remote._
import common._
import mapreduce._

object Client1 extends App {
  val system = ActorSystem("ClientSystem", ConfigFactory.load.getConfig("remotelookup"))
  var numberServers = 0
  var master: ActorRef = null
  var mappers: Array[ActorRef] = null
  var reducers: Array[ActorRef] = null
  var urls: List[String] = null
  urls = "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg807.txt" :: Nil
  urls = "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg98.txt" :: urls
  urls = "http://reed.cs.depaul.edu/lperkovic/csc536/homeworks/gutenberg/pg20795.txt" :: urls

  println("Running the first mapper/reducer which count the number of occurrences of words in a set of text files.")
  init
  val mapfunc = new WordCountMap()
  setMapper(mapfunc)
  val reducefunc = new WordCountReduce()
  setReducer(reducefunc)
  for (url <- urls)
    master ! url
  Thread.sleep(500)
  master ! Flush
  Thread.sleep(5000)
  system.terminate()
  
  def setMapper(f: MapTrait) = {
    for (i <- 0 until numberServers) {
      mappers(i) ! f
    }
  }
  
  def setReducer(f: ReduceTrait) = {
    for (i <- 0 until numberServers) {
      reducers(i) ! f
    }
  }
  
  def init = {
    // Read application configuration
    numberServers = ConfigFactory.load.getInt("number-servers")
    var serverUrls = new Array[String](numberServers)
    for (i <- 0 until numberServers) {
      serverUrls(i) = ConfigFactory.load.getString("server" + i)
    }
    // Create MasterActor
    master = system.actorOf(Props[MasterActor].withDeploy(Deploy(scope = RemoteScope(AddressFromURIString(serverUrls(0))))))
    master ! SetNumReducer(numberServers)
    println("MasterActor: " + master)
    // Create MapActors and ReduceActors
    reducers = new Array[ActorRef](numberServers)
    mappers = new Array[ActorRef](numberServers)
    for (i <- 0 until numberServers) {
      reducers(i) = system.actorOf(Props[ReduceActor].withDeploy(Deploy(scope = RemoteScope(AddressFromURIString(serverUrls(i))))))
      reducers(i) ! SetNumMapper(numberServers)
      reducers(i) ! SetMaster(master)
      println("ReduceActor" + i + ":" + reducers(i))
    }
    for (i <- 0 until numberServers) {
      mappers(i) = system.actorOf(Props[MapActor].withDeploy(Deploy(scope = RemoteScope(AddressFromURIString(serverUrls(i))))))
      mappers(i) ! SetReducers(reducers)
      println("MapActor" + i + ":" + mappers(i))
    }
    master ! SetMappers(mappers)
  
  }
}

