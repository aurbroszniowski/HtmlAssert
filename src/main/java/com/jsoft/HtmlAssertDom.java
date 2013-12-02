package com.jsoft;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Aurelien Broszniowski
 */
public class HtmlAssertDom {

  private Document doc;
  private Parsing parsing;
  private TagsFinder tagsFinder = new TagsFinder();
  private List<Tag> tagsList = new LinkedList<Tag>();

  public HtmlAssertDom(final Document doc, final Parsing parsing) {
    this.doc = doc;
    this.parsing = parsing;
  }

  public HtmlAssertDom(final String html, final Parsing parsing) {
    if (html == null) {
      throw new NullPointerException("Can not Assert null HTML content");
    }
    this.doc = Jsoup.parse(html, "", Parser.xmlParser());
    this.parsing = parsing;
  }

  public HtmlAssertDom(final String html) {
    this(html, Parsing.LENIENT);
  }

  public static void it(String description, HtmlAssertDom htmlAssertDom) {
    boolean found = htmlAssertDom.processTagsList();
    if (!found) {
      throw new AssertionError(description + " didn't find the tags");
    }
  }

  private boolean processTagsList() {
    Element currentNode = this.doc;

    int index = 0;
    return findTags(currentNode, index);
  }

  private boolean findTags(final Element currentNode, int index) {
    if (index == tagsList.size()) { // if we reach this, then we found all tags in stack
      return true;
    }
    Tag tag = tagsList.get(index);
    Map<String, String> attributesMap = getAttributes(tag);

    Elements elements = currentNode.getElementsByTag(tag.getTag());
    removeUnmatchingElements(elements, attributesMap);
    boolean oneExist = false;
    index++;
    for (Element element : elements) {
      oneExist |= findTags(element, index);
    }

    return oneExist;
  }

  private Map<String, String> getAttributes(final Tag tag) {
    final String[] attributes = tag.getAttributes();
    if ((attributes.length % 2) == 1) {
      throw new AssertionError(tag + " attributes should be defined in pair " + tag + "(\"name\", \"value\", ...)");
    }
    Map<String, String> attributesMap = new HashMap<String, String>();
    for (int i = 0; i < attributes.length; i += 2) {
      final String attributeName = attributes[i];
      final String attributeValue = attributes[i + 1];
      attributesMap.put(attributeName, attributeValue);
    }
    return attributesMap;
  }

  private void removeUnmatchingElements(final Elements elements, final Map<String, String> attributesMap) {
    List<Element> elementsToRemove = new ArrayList<Element>();
    for (Element element : elements) {
      Map<String, String> matchedAttributesMap = new HashMap<String, String>();
      Attributes attributes = element.attributes();
      for (Attribute attribute : attributes) {
        matchedAttributesMap.put(attribute.getKey(), attribute.getValue());
      }
      if (!hashMapsAreEqual(attributesMap, matchedAttributesMap)) {
        elementsToRemove.add(element);
      }
    }
    elements.removeAll(elementsToRemove);
  }

  public static void describe(String description, HtmlAssertDom htmlAssertDom) {

  }

  /**
   * Tags
   */
  public HtmlAssertDom a(final String... attributes) {
    return tag("a", attributes);
  }

  public HtmlAssertDom b(final String... attributes) {
    return tag("b", attributes);
  }

  public HtmlAssertDom br(final String... attributes) {
    return tag("br", attributes);
  }

  public HtmlAssertDom div(final String... attributes) {
    return tag("div", attributes);
  }

  public HtmlAssertDom span(final String... attributes) {
    return tag("span", attributes);
  }

  public HtmlAssertDom table(final String... attributes) {
    return tag("table", attributes);
  }

  public HtmlAssertDom td(final String... attributes) {
    return tag("td", attributes);
  }

  public HtmlAssertDom th(final String... attributes) {
    return tag("th", attributes);
  }

  public HtmlAssertDom tr(final String... attributes) {
    return tag("tr", attributes);
  }

  // plain text
  public HtmlAssertDom text(final String text) {
    throw new UnsupportedOperationException("To implement");
  }

  private HtmlAssertDom tag(final String tag) {
    this.tagsList.add(new Tag(tag));
    return this;
  }

  private HtmlAssertDom tag(final String tag, final String... attributes) {
    this.tagsList.add(new Tag(tag, attributes));
    return this;
  }
/*
  private HtmlAssertDom process(final Tag entryTag) {
    final String tag = entryTag.getTag();
    final String[] attributes = entryTag.getAttributes();

    if (attributes.length == 0) {
      return process(tag);
    }

    if ((attributes.length % 2) == 1) {
      throw new AssertionError(tag + " attributes should be defined in pair " + tag + "(\"name\", \"value\", ...)");
    }
    Map<String, String> attributesMap = new HashMap<String, String>();
    for (int i = 0; i < attributes.length; i += 2) {
      final String attributeName = attributes[i];
      final String attributeValue = attributes[i + 1];
      attributesMap.put(attributeName, attributeValue);
    }

    TagsFinder.TraverseResult result = tagsFinder.traverse(node, tag, attributesMap);
    return assertTagPosition("<" + tag + ">", result.getPosition(), result.getNode());
  }

  private HtmlAssertDom process(final String tag) {
    TagsFinder.TraverseResult result = tagsFinder.traverse(node, tag);
    return assertTagPosition("<" + tag + ">", result.getPosition(), result.getNode());
  }

  private HtmlAssertDom assertTagPosition(final String tag, final int position, final Node node) {
    if (parsing == Parsing.STRICT && position == 0) {
      return new HtmlAssertDom(node, parsing);
    }
    if (parsing == Parsing.LENIENT && position > -1) {
      return new HtmlAssertDom(node, parsing);
    }
    throw new AssertionError(tag + " is not present");
  }*/

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

}