package com.jsoft;

import org.jsoup.nodes.Node;

/**
 * Custom version of NodeTraversor where the traversor stops when it encounters the tag, and gives back the Node and
 * its position in the DOM.
 * No visitor needed.
 *
 * @author Aurelien Broszniowski
 */
public class TagsFinder {

  public TagsFinder() {
  }

  /**
   * Start a depth-first traverse of the root and all of its descendants.
   *
   * @param root the root node point to traverse.
   */
  public TraverseResult traverse(Node root, String tag) {
    Node node = root;
    int depth = 0;
    int position = 0;

    while (node != null) {
      if (tag.equalsIgnoreCase(node.nodeName())) {
        return new TraverseResult(node, position);
      }
      position++;
      if (node.childNodeSize() > 0) {
        node = node.childNode(0);
        depth++;
      } else {
        while (node.nextSibling() == null && depth > 0) {
          node = node.parentNode();
          depth--;
        }
        if (node == root) {
          position = -1;
          break;
        }
        node = node.nextSibling();
      }
    }
    return new TraverseResult(node, position);
  }

  public static class TraverseResult {
    private Node node;
    private int position;

    public TraverseResult(final Node node, final int position) {
      this.node = node;
      this.position = position;
    }

    public Node getNode() {
      return node;
    }

    public int getPosition() {
      return position;
    }
  }
}
