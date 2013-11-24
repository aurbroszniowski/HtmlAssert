package com.jsoft;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DSL based assertion utility to assert HTML path and content
 * e.g.
 * <p/>
 * <table id="mytable">
 * <tr class="someclass">
 * <td class="someclass" id="sometd">content</td>
 * </tr>
 * </table>
 * <p/>
 * Will be asserted by
 * String html = ... // content above
 * HtmlAssert htmlAssert = new HtmlAssert(html);
 * htmlAssert.table(id,"mytable").tr(class,"someclass").td(class,"someclass", id,"sometd").text("content");
 * <p/>
 * all parameters are optional, in case they are blank, HtmlAssert will try to find itself the path,
 * e.g. the following should work :
 * htmlAssert.table().tr().td().text("content");
 * htmlAssert.text("content");
 * <p/>
 *
 * @author Aurelien Broszniowski
 */
public class HtmlAssert {

  private String html;
  private Parsing parsing;

  public HtmlAssert(final String html, final Parsing parsing) {
    if (html == null) {
      throw new NullPointerException("Can not Assert null HTML content");
    }
    this.html = html.trim();
    this.parsing = parsing;
  }

  public HtmlAssert(final String html) {
    this(html, Parsing.LENIENT);
  }

  /**
   * Tags
   */
  public HtmlAssert a(final String... attributes) {
    return tag("a", attributes);
  }

  public HtmlAssert b(final String... attributes) {
    return tag("b", attributes);
  }

  public HtmlAssert br(final String... attributes) {
    return tag("br", attributes);
  }

  public HtmlAssert div(final String... attributes) {
    return tag("div", attributes);
  }

  public HtmlAssert span(final String... attributes) {
    return tag("span", attributes);
  }

  public HtmlAssert table(final String... attributes) {
    return tag("table", attributes);
  }

  public HtmlAssert td(final String... attributes) {
    return tag("td", attributes);
  }

  public HtmlAssert th(final String... attributes) {
    return tag("th", attributes);
  }

  public HtmlAssert tr(final String... attributes) {
    return tag("tr", attributes);
  }

  //**********************************************************

  private HtmlAssert tag(final String tag) {
    String fullTag = "<" + tag + ">";
    int pos = html.toLowerCase().indexOf(fullTag);
    return assertTagPosition(fullTag, pos, fullTag.length());
  }

  private HtmlAssert tag(final String tag, final String... attributes) {
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

    Pattern tagsPattern = Pattern.compile("(<" + tag + ".*?>)", Pattern.DOTALL);
    Matcher tagsMatcher = tagsPattern.matcher(html);
    while (tagsMatcher.find()) {
      Map<String, String> matchedAttributesMap = new HashMap<String, String>();
      String currentTag = html.substring(tagsMatcher.start(), tagsMatcher.end());
      Pattern attributesPattern = Pattern.compile("(\\w+)(\\=\"*[a-zA-Z0-9_\\-\\:\\+\\.\\(\\); ]+\"*)*", Pattern.MULTILINE);
      Matcher attributesMatcher = attributesPattern.matcher(currentTag);
      while (attributesMatcher.find()) {
        if (!attributesMatcher.group(0).equalsIgnoreCase(tag)) {
          String[] attrs = attributesMatcher.group(0).replace("\"", "").replace("\'", "").split("=");
          if (attrs.length == 2) {
            matchedAttributesMap.put(attrs[0], attrs[1]);
          } else {
            matchedAttributesMap.put(attrs[0], null);
          }
        }
      }

      if (hashMapsAreEqual(attributesMap, matchedAttributesMap)) {
        int pos = tagsMatcher.start();
        return assertTagPosition("<" + tag + ">", pos, currentTag.length());
      }
    }
    return assertTagPosition("<" + tag + ">", -1, -1);
  }

  private HtmlAssert assertTagPosition(final String tag, final int position, final int length) {
    if (parsing == Parsing.STRICT && position == 0) {
      return new HtmlAssert(html.substring(position + length), parsing);
    }
    if (parsing == Parsing.LENIENT && position > -1) {
      return new HtmlAssert(html.substring(position + length), parsing);
    }
    throw new AssertionError(tag + " is not present");
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
}
