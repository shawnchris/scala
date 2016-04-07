object MatchTest2 extends App {
  def matchTest(x: Any): Any = x match {
    case 1 => "one"
    case "two" => 2
    case y: Int => "scala.Int: " + y
  }
  println(matchTest(1))
  println(matchTest("two"))
  println(matchTest(3))
  println(matchTest("four"))
}
