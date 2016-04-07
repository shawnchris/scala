import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool

case class FileSize(size : Long)
case class DirToProcess(fileName : String)

class FileProcessor extends Actor {
  def receive = {
    case DirToProcess(fileName) =>
      val file = new java.io.File(fileName)
      var size = 0L
      val children = file.listFiles()
      if (children != null)
        for (child <- children)
          if (child.isFile())
            size += child.length()
          else
            sender ! DirToProcess(child.getPath())
      
      sender ! FileSize(size)
  }
}

class Collector extends Actor {
    var totalSize = 0L
    var pending = 0
    val start = System.nanoTime()

    val workerRouter = context.actorOf(RoundRobinPool(50).props(Props[FileProcessor]), name = "workerRouter")

  def receive = {
    case DirToProcess(fileName) =>
      workerRouter ! DirToProcess(fileName)
      pending = pending + 1
    case FileSize(size) =>
      totalSize += size
      pending = pending - 1
      if (pending == 0) {
        val end = System.nanoTime()
        println("Total size is " + totalSize)
        println("Time taken is " + (end - start)/1.0e9)        
        context.system.terminate()
      }
  }
}

object AkkaFileSize {
  def main(args : Array[String]) : Unit = {
    val system = ActorSystem("FileSizeSystem");
    val collector = system.actorOf(Props[Collector])
    collector ! DirToProcess(args(0))
  }
}
