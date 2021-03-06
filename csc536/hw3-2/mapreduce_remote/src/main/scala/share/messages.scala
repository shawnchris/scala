package share

import akka.actor.{Actor, ActorRef, Props}
case class Message(title: String, url: String)
case class Pair(name: String, title: String)
case class AddMapper(actor:List[ActorRef])
case class AddReducer(actors:List[ActorRef],client:ActorRef)
case class GetReducer(actor:List[ActorRef])
case object Start
case object Flush
case object Done
