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
 * TODO : Add a strict mode to make sure that the path MUST follow the exact definition
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

  public HtmlAssert div() {
    int pos = html.toLowerCase().indexOf("<div>");
    return assertTagPosition(pos);
  }

  public HtmlAssert div(final String... attributes) {
    if ((attributes.length % 2) == 1) {
      throw new AssertionError("div attributes should be defined in pair div(\"name\", \"value\", ...)");
    }
    Map<String, String> attributesMap = new HashMap<String, String>();
    for (int i = 0; i < attributes.length; i += 2) {
      final String attributeName = attributes[i];
      final String attributeValue = attributes[i+1];
      attributesMap.put(attributeName, attributeValue);
    }

    Pattern tagsPattern = Pattern.compile("(<div.*?>)");
    Matcher tagsMatcher = tagsPattern.matcher(html);
    while (tagsMatcher.find()) {
      Map<String, String> matchedAttributesMap = new HashMap<String, String>();
      String currentTag = html.substring(tagsMatcher.start(), tagsMatcher.end());  // id="ss" class="cdcd" char=ssj
      Pattern attributesPattern = Pattern.compile("(\\w+)(\\=\"*(\\w+)\"*)*");
      Matcher attributesMatcher = attributesPattern.matcher(currentTag);
      while (attributesMatcher.find()) {
        if (!attributesMatcher.group(0).equalsIgnoreCase("div")) {
          matchedAttributesMap.put(attributesMatcher.group(1), attributesMatcher.group(3));
        }
      }

      if (hashMapsAreEqual(attributesMap, matchedAttributesMap)) {
        int pos = tagsMatcher.start();
        return assertTagPosition(pos);
      }
    }
    return assertTagPosition(-1);
  }

  private HtmlAssert assertTagPosition(final int position) {
    if (parsing == Parsing.STRICT && position == 0) {
      return new HtmlAssert(html.substring(position + "<div>".length()), parsing);
    }
    if (parsing == Parsing.LENIENT && position > -1) {
      return new HtmlAssert(html.substring(position + "<div>".length()), parsing);
    }
    throw new AssertionError("<div> is not present");
  }

  private boolean hashMapsAreEqual(final Map<String, String> map1, final Map<String, String> map2) {
    if (map1.size() != map2.size()) {
      return false;
    }
    for (String s : map1.keySet()) {
      if (map1.get(s) == null) {
        if (map2.get(s) != null) {
          return false;
        }
      } else if (!map1.get(s).equalsIgnoreCase(map2.get(s))) {
        return false;
      }
    }
    return true;
  }
}
