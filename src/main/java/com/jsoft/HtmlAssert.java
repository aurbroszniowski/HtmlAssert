package com.jsoft;

import com.jsoft.attributes.HtmlAttribute;

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
 * htmlAssert.table(id="mytable").tr(class="someclass").td(class="someclass", id="sometd").text("content");
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

/*
  public HtmlAssert div(HtmlAttribute attribute) {
    // from attribute reconstruct html (<div attr=""...) order alphabetically
    // search all "<div nb_attr"
    // iterate and return position for one with all attributes

  }
*/

  public HtmlAssert div() {
    int pos = html.toLowerCase().indexOf("<div>");
    if (parsing == Parsing.STRICT && pos == 0) {
      return new HtmlAssert(html.substring(pos + "<div>".length()), parsing);
    }
    if (parsing == Parsing.LENIENT && pos > -1) {
      return new HtmlAssert(html.substring(pos + "<div>".length()), parsing);
    }
    throw new AssertionError("<div> is not present");
  }
}
