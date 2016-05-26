package mapreduce
import common._
import scala.io.Source
import scala.collection.mutable.HashMap

class NameCountMap extends MapTrait {
  def process(str: String): List[Pair] = {
    val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
      "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
    var result = List[Pair]();
    val idx = str.indexOf("\t")
    val title = str.substring(0, idx)
    val url = str.substring(idx + 1)
    val content = Source.fromURL(url).mkString
    for (name <- content.split("[\\p{Punct}\\s]+")) {
      if ((!STOP_WORDS_LIST.contains(name.toLowerCase)) && name.matches("\\p{Upper}[\\p{Lower}]*")) {
        result = Pair(name, title) :: result
      }
    }
    return result
  }
}

class NameCountReduce extends ReduceTrait {
  def process(p: Pair, m: HashMap[String, String]) = {
    var value = p.value
    if (m.contains(p.key) && m(p.key).indexOf(p.value) < 0)
      value += "; " + m(p.key)
    m += (p.key -> value)
  }
}