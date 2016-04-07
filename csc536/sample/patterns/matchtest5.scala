sealed trait Op
case object OpAdd extends Op
case object OpSub extends Op
case object OpMul extends Op
case object OpDiv extends Op

sealed trait Exp 
case class ExpNum (n:Double) extends Exp
case class ExpOp (e1:Exp, op:Op, e2:Exp) extends Exp

object MatchTest5 extends App {

  def evaluate (e:Exp) : Double = e match {
    case ExpNum (v) => v
    case ExpOp (e1, op, e2) => 
      val n1:Double = evaluate (e1)
      val n2:Double = evaluate (e2)
      op match {
        case OpAdd => n1 + n2
        case OpSub => n1 - n2
        case OpMul => n1 * n2
        case OpDiv => n1 / n2
      }
  }

  println(evaluate(ExpNum(3.14)))
  println(evaluate(ExpOp(ExpNum(3.14), OpAdd, ExpNum(4.0))))
  println(evaluate(ExpOp(ExpOp(ExpNum(3.14), OpAdd, ExpNum(4.0)), OpMul, ExpNum(2.0))))
}
