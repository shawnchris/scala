import scala.collection.mutable

object A0017LetterCombinationsOfPhoneNumber extends App {
  def letterCombinations(digits: String): List[String] = {
    val dict = Array("", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz")
    val results = scala.collection.mutable.ListBuffer.empty[String]
    if (digits.isEmpty) {
      return results.toList
    }
    helper(0, new mutable.StringBuilder)

    def helper(index: Int, current: mutable.StringBuilder): Unit = {
      if (index == digits.length()) {
        results += current.toString
        return
      }

      val candidates = dict(digits.charAt(index) - '0')
      for (i <- 0 until candidates.length()) {
        current.append(candidates.charAt(i))
        helper(index + 1, current)
        current.deleteCharAt(current.size - 1)
      }
    }

    results.toList
  }
}
