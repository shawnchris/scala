import scala.collection.mutable.ListBuffer

object A0039CombinationSum extends App {
  def combinationSum(candidates: Array[Int], target: Int): List[List[Int]] = {
    val sortedCandidates = candidates.sorted
    val res = ListBuffer.empty[List[Int]]

    helper(0, ListBuffer.empty[Int], 0)

    def helper(sum:Int, current: ListBuffer[Int], start: Int): Unit = {
      if (sum == target) {
        res += current.toList
        return
      }
      for (i <- start until sortedCandidates.length) {
        val s = sum + sortedCandidates(i)
        if (s > target) {
          return
        }
        current += sortedCandidates(i)
        helper(s, current, i)
        current.remove(current.length - 1)
      }
    }

    res.toList
  }
}
