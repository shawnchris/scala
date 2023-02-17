object A0146LruCache extends App {

  class LRUCache(_capacity: Int) {

    class Node(_key: Int, _value: Int) {
      var key: Int = _key
      var value: Int = _value
      var prev: Node = null
      var next: Node = null
    }

    val capacity: Int = _capacity
    var size: Int = 0
    val map = scala.collection.mutable.HashMap.empty[Int, Node]
    val head = new Node(Int.MaxValue, Int.MaxValue)
    val tail = new Node(Int.MinValue, Int.MinValue)
    head.next = tail
    tail.prev = head

    def get(key: Int): Int = {
      val node = map.get(key)
      if (node.isEmpty) {
        -1
      } else {
        moveToHead(node.get)
        node.get.value
      }
    }

    def put(key: Int, value: Int): Unit = {
      val node = map.get(key)
      if (node.isEmpty) { // doesn't exist
        if (size >= capacity) {
          removeLast() // evict the last one
        }
        val newNode = new Node(key, value)
        map.put(key, newNode)
        putToHead(newNode)
        size += 1
      } else {
        node.get.value = value
        moveToHead(node.get) // update freshness
      }
    }

    def putToHead(node: Node): Unit = {
      node.prev = head
      node.next = head.next
      head.next.prev = node
      head.next = node
    }

    def moveToHead(node: Node): Unit = {
      if (node.prev != head) {
        node.prev.next = node.next
        node.next.prev = node.prev
        putToHead(node)
      }
    }

    def removeLast(): Unit = {
      val toRemove = tail.prev
      tail.prev = toRemove.prev
      toRemove.prev.next = toRemove.next
      map.remove(toRemove.key)
      size -= 1
    }
  }
}
