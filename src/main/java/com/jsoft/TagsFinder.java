package com.jsoft;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

  public TraverseResult traverse(final Node root, final String tag, final Map<String, String> attributesMap) {
    Node node = root;
    int depth = 0;
    int position = 0;

    while (node != null) {
      if (tag.equalsIgnoreCase(node.nodeName())) {
        Map<String, String> matchedAttributesMap = new HashMap<String, String>();
        Attributes attributes = node.attributes();
        for (Attribute attribute : attributes) {
          matchedAttributesMap.put(attribute.getKey(), attribute.getValue());
        }
        if (hashMapsAreEqual(attributesMap, matchedAttributesMap)) {
          return new TraverseResult(node, position);
        }
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

  protected boolean hashMapsAreEqual(final Map<String, String> map1, final Map<String, String> map2) {
    if (map1.size() != map2.size()) {
      return false;
    }
    for (String s : map1.keySet()) {
      if (map1.get(s) == null) {
        if (map2.get(s) != null) {
          return false;
        }
      } else if (map1.get(s).contains("*")) {
        String value1 = Pattern.quote(map1.get(s)).replace("*", "\\E.*\\Q");
        if (!Pattern.matches(value1, map2.get(s))) {
          return false;
        }
      } else if (!map1.get(s).equalsIgnoreCase(map2.get(s))) {
        return false;
      }
    }
    return true;
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
