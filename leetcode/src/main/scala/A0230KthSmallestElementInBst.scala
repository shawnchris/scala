object A0230KthSmallestElementInBst extends App {
  def kthSmallest(root: TreeNode, k: Int): Int = {
    var result: Int = 0
    var left: Int = k

    def inOrder(root: TreeNode): Unit = {
      if (root == null) {
        return
      }
      if (left == 0) {
        result = root.value
        return
      }
      inOrder(root.left)
      left -= 1
      inOrder(root.right)
    }
    inOrder(root)

    result
  }
}
