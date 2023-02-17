class A0056MergeIntervals extends App {
  def merge(intervals: Array[Array[Int]]): Array[Array[Int]] = {
    var results: Array[Array[Int]] = Array()
    val sortedIntervals = intervals.sorted((a: Array[Int], b: Array[Int]) => (a(0) - b(0)))
    var start = sortedIntervals(0)(0)
    var end = sortedIntervals(0)(1)
    for (i <- 1 until sortedIntervals.length) {
      val interval = sortedIntervals(i)
      if (interval(0) <= end) { // merge
        end = Math.max(end, interval(1))
      } else { // save to results
        results = results :+ Array(start, end)
        start = interval(0)
        end = interval(1)
      }
    }
    results = results :+ Array(start, end)

    results
  }
}
