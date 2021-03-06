package mapreduce

case class Message(title: String, url: String)
case class RetryMessage(title: String, url: String)
case class Pair(name: String, title: String)
case object Flush
case object Done
