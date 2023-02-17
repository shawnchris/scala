object A0049GroupAnagrams extends App {
  def groupAnagrams(strs: Array[String]): List[List[String]] = {
    if (strs.length == 0) {
      return List()
    }

    val map = scala.collection.mutable.HashMap.empty[String, List[String]]
    for (str <- strs) {
      val key = str.toCharArray.sorted.mkString
      val value = map.getOrElse(key, List()) :+ str
      map.put(key, value)
    }
    map.values.toList
  }
}
