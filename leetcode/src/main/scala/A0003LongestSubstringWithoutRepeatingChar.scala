object A0003LongestSubstringWithoutRepeatingChar extends App {
  def lengthOfLongestSubstring(s: String): Int = {
    var left = 0
    var max = 0
    val set = scala.collection.mutable.HashSet.empty[Char]
    for (right <- 0 until s.length()) {
      while (left < right && set.contains(s.charAt(right))) {
        set -= s.charAt(left)
        left += 1
      }
      set.add(s.charAt(right))
      max = Math.max(max, right - left + 1)
    }
    max
  }
}
