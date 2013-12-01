package com.jsoft;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aurelien Broszniowski
 */
public class HtmlAssertDom {

  private Node node;
  private Parsing parsing;
  private TagsFinder tagsFinder = new TagsFinder();

  public HtmlAssertDom(final Node node, final Parsing parsing) {
    this.node = node;
    this.parsing = parsing;
  }

  public HtmlAssertDom(final String html, final Parsing parsing) {
    if (html == null) {
      throw new NullPointerException("Can not Assert null HTML content");
    }
    Document doc = Jsoup.parse(html, "", Parser.xmlParser());
    Elements select = doc.select("*");
    node = select.first();
    this.parsing = parsing;
  }

  public HtmlAssertDom(final String html) {
    this(html, Parsing.LENIENT);
  }

  public static void it(String description, HtmlAssertDom htmlAssertDom) {

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
  public HtmlAssert text(final String text) {
    throw new UnsupportedOperationException("To implement");
  }

  private HtmlAssertDom tag(final String tag, final String... attributes) {
    if (attributes.length == 0) {
      return tag(tag);
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

  private HtmlAssertDom tag(final String tag) {
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
  }

}