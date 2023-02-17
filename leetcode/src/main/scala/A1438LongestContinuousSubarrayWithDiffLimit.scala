import scala.collection.mutable

object A1438LongestContinuousSubarrayWithDiffLimit extends App {
  def longestSubarray(nums: Array[Int], limit: Int): Int = {
    var max = 0
    var left = 0
    // Key is the value of an array element; value is number of occurrence of this value.
    val map = mutable.TreeMap.empty[Int, Int]

    for (right <- nums.indices) {
      map.put(nums(right), map.getOrElse(nums(right), 0) + 1)
      while (map.lastKey - map.firstKey > limit) {
        map.put(nums(left), map(nums(left)) - 1)
        if (map(nums(left)) <= 0) {
          map.remove(nums(left))
        }
        left += 1
      }
      max = Math.max(max, right - left + 1)
    }

    max
  }
}
