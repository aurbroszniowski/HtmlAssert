package com.jsoft;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

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
    Document doc = Jsoup.parse(html);
    Elements select = doc.select("*");
    node = select.first();
    this.parsing = parsing;
  }

  public HtmlAssertDom(final String html) {
    this(html, Parsing.LENIENT);
  }

  /**
   * Tags
   */

  public HtmlAssertDom div(final String... attributes) {
    return tag("div", attributes);
  }

  private HtmlAssertDom tag(final String tag, final String... attributes) {
    if (attributes.length == 0) {
      return tag(tag);
    }

    if ((attributes.length % 2) == 1) {
      throw new AssertionError(tag + " attributes should be defined in pair " + tag + "(\"name\", \"value\", ...)");
    }

    return assertTagPosition("<" + tag + ">", -1, this.node);
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