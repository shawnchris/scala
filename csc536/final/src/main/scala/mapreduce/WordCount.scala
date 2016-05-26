package mapreduce
import common._
import scala.io.Source
import scala.collection.mutable.HashMap

class WordCountMap extends MapTrait {
  def process(url: String): List[Pair] = {
    val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
      "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
    var result = List[Pair]();
    val content = Source.fromURL(url).mkString
    for (word <- content.toLowerCase.split("[\\p{Punct}\\s]+")) {
      if ((!STOP_WORDS_LIST.contains(word))) {
        result = Pair(word, "1") :: result
      }
    }
    return result
  }
}

class WordCountReduce extends ReduceTrait {
  def process(p: Pair, m: HashMap[String, String]) = {
    var value = "1"
    if (m.contains(p.key))
      value = String.valueOf((Integer.valueOf(m(p.key)) + 1))
    m += (p.key -> value)
    
  }
}