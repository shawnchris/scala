class TrieNode (_next: Array[TrieNode] = null, _isWord: Boolean = false) {
  val R = 26
  var next: Array[TrieNode] = _next
  var isWord: Boolean = _isWord
}
