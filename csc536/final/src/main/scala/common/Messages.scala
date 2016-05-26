package common
import akka.actor.ActorRef

case class Pair(key: String, value: String)
case object Flush
case object Done
case class SetNumMapper(num: Int)
case class SetNumReducer(num: Int)
case class SetMappers(mappers: Array[ActorRef])
case class SetReducers(reducers: Array[ActorRef])
case class SetMaster(master: ActorRef)
