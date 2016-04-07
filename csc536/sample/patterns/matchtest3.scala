case class Start(secondPath : String)
case object PING
case object PONG

object MatchTest3 extends App {
  def matchTest(x: Any): Any = x match {
    case Start(secondPath) => "got " + secondPath
    case PING => "got ping"
    case PONG => "got pong"
  }
  println(matchTest(Start("path")))
  println(matchTest(PING))
  println(matchTest(PONG))
}
