package mapreduce
import common._
import scala.io.Source
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class LinkCountMap extends MapTrait {
  def process(url: String): List[Pair] = {
    var result = List[Pair]();
    var targets = HashSet[String]()
    targets += "http://doc.akka.io/docs/akka/current/scala.html"
    targets += "http://doc.akka.io/docs/akka/current/java.html"
    targets += "https://d2l.depaul.edu/"
    targets += "http://doc.akka.io/docs/akka/current/"
    targets += "http://docs.oracle.com/javase/8/docs/api/"
    targets += "http://www.scala-lang.org/api/current/index.html"
    
    val content = Source.fromURL(url).mkString
    var i = -1
    var j = -1
    do {
      i = content.indexOf("href=\"", j + 1)
      if (i >= 0)
        j = content.indexOf("\"", i + 6)
      if (j >= 0) {
        val link = content.substring(i + 6, j)
        if (targets.contains(link))
          result = Pair(link, "1") :: result
      }
    } while (i >= 0 && j >= 0)
    
    return result
  }
}

class LinkCountReduce extends ReduceTrait {
  def process(p: Pair, m: HashMap[String, String]) = {
    var value = "1"
    if (m.contains(p.key))
      value = String.valueOf((Integer.valueOf(m(p.key)) + 1))
    m += (p.key -> value)
    
  }
}