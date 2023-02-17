import scala.collection.mutable

object A0353SnakeGame extends App {
  class SnakeGame(_width: Int, _height: Int, _food: Array[Array[Int]]) {
    val width: Int = _width
    val height: Int = _height
    val food: Array[Array[Int]] = _food
    val snakeSet: mutable.HashSet[(Int, Int)] = mutable.HashSet.empty[(Int, Int)]
    val snakeList: mutable.ListBuffer[(Int, Int)] = mutable.ListBuffer.empty[(Int, Int)]
    var foodIndex = 0

    val startCell: (Int, Int) = (0, 0)
    snakeSet += startCell
    snakeList += startCell

    def move(direction: String): Int = {
      var (headRow, headCol) = snakeList.head
      direction match {
        case "U" => headRow -= 1
        case "D" => headRow += 1
        case "L" => headCol -= 1
        case "R" => headCol += 1
      }
      val newHeadCell = (headRow, headCol)
      val tailCell = snakeList.remove(snakeList.length - 1)
      snakeSet.remove(tailCell)

      val crossesBoundary = headRow < 0 || headRow >= this.height || headCol < 0 || headCol >= this.width
      val bitesItself = snakeSet.contains(newHeadCell)
      if (crossesBoundary || bitesItself) return -1

      // If there's an available food item and it is on the cell occupied by the snake after the move,
      // eat it and add the tail back.
      if ((foodIndex < this.food.length) && (food(foodIndex)(0) == headRow) && (food(foodIndex)(1) == headCol)) {
        this.foodIndex += 1
        this.snakeList += tailCell
        this.snakeSet += tailCell
      }

      snakeList.insert(0, newHeadCell)
      snakeSet += newHeadCell

      snakeList.length - 1
    }
  }
}
