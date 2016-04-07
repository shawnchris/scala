object MatchTest4 extends App {
  def length [X] (xs:List[X]): Int = xs match {
    case Nil     => 0
    case y :: ys => 1 + length(ys)
  }
  println(length(List()))
  println(length(List(1,2)))
  println(length(List("one", "two", "three")))
}
