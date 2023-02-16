object A0001TwoSum extends App {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val map = nums.view.zipWithIndex.toMap
    for (i <- nums.indices) {
      if (map.contains(target - nums(i)) && i != map(target - nums(i))) {
        return Array(i, map(target - nums(i)))
      }
    }
    Array()
  }

  println(twoSum(Array(2, 7, 11, 15), 9).mkString("Array(", ", ", ")"))
  println(twoSum(Array(3,2,4), 6).mkString("Array(", ", ", ")"))
  println(twoSum(Array(3,3), 6).mkString("Array(", ", ", ")"))
}
