package com.jsoft;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jsoft.HtmlAssertDom.it;

/**
 * //TODO: try static attributes ?
 *
 * @author Aurelien Broszniowski
 */
public class HtmlAssertTest {

  @Test
  public void testPassingDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\" hidden><div class=\"someclass\" ><div><table></table></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);

    htmlAssert.div("id", "someid", "class", "someclass", "hidden", null).div("class", "someclass").table();
    htmlAssert.div("hidden", null, "class", "someclass", "id", "someid");
  }

  @Test
  public void testAttributeMultipleLines() {
    String html = " <div>           <td title=\"en-gb\"\n" +
                  "                style=\"166px;\">en-gb</td></div>\n";
    String htmlSingleLine = "<div>           <td title=\"en-gb\"                 style=\"166px;\">en-gb</td></div>\n";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);

    String tag = "td";
    Pattern tagsPattern = Pattern.compile("(<" + tag + ".*?>)", Pattern.DOTALL);
    Matcher m = tagsPattern.matcher(html);
    int nbTags = 0;
    while (m.find()) {
      String currentTag = html.substring(m.start(), m.end());
      nbTags++;

      Pattern attributesPattern = Pattern.compile("(\\w+)(\\=\"*[a-zA-Z0-9_\\-\\:\\+\\.\\(\\); ]+\"*)*", Pattern.MULTILINE);
      Matcher attributesMatcher = attributesPattern.matcher(currentTag);
      int attributesCnt = 0;
      while (attributesMatcher.find()) {
        attributesCnt++;
      }
      Assert.assertEquals(3, attributesCnt);
    }
    Assert.assertEquals(1, nbTags);

    it("tests attribute on multiple lines HTML",
        htmlAssert.td("title", "en-gb", "style", "166px;"));
  }

  @Test
  public void testAttributeWithSpaces() {
    String html = " <td class=\"main_column main_column1\" title=\"1.26e+3k  (1257592)\" accesskey title=\"ar\" style=\"min-width: 166px; width: 166px; max-width: 166px;\"></td>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);

    it("tests attributes with spaces",
        htmlAssert.td("class", "main_column main_column1", "title", "ar", "style", "min-width: 166px; width: 166px; max-width: 166px;", "accesskey", ""));
  }

  @Test
  public void testSimilarMultipleLines() {
    String html = "<div><table><tr><div class=\"somediv\"><tr><span id=\"someid\"><div id=\"id1\"><td></td></div></span></tr><div>" +
                  "<div class=\"somediv\"><tr><span id=\"someid\"><div id=\"id2\"><td></td></div></span></tr><div>" +
                  "<div class=\"somediv\"><tr><span id=\"someid\"><div id=\"id3\"><td></td></div></span></tr>" +
                  "<div></div></div></div></div></div></div></tr> </table></div>";

    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);

    it("will test smthing",
        htmlAssert.tr().div("class", "somediv").tr().span("id", "someid").div("id", "id2"));
  }

  // Empty Div tests

  @Test
  public void testPassingLenientEmptyDiv() {
    String html = "<dZv><div></div></dZv>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests PassingLenientEmptyDiv",
        htmlAssert.div()
    );
  }

  @Test
  public void testPassingStrictEmptyDiv() {
    String html = "<div><div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testPassingStrictEmptyDiv",
        htmlAssert.div()
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientEmptyDiv() {
    String html = "<dZv></dZv>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.LENIENT);
    it("tests testFailingLenientEmptyDiv",
        htmlAssert.div()
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictEmptyDiv() {
    String html = "<tr><div></div></tr>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testFailingStrictEmptyDiv",
        htmlAssert.div()
    );
  }

  // Filled Div tests

  @Test
  public void testPassingLenientOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testPassingLenientOrderedFilledDiv",
        htmlAssert.div("id", "someid", "class", "someclass")
    );
  }

  @Test
  public void testPassingLenientUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testPassingLenientUnorderedFilledDiv",
        htmlAssert.div("class", "someclass", "id", "someid")
    );
  }

  @Test
  public void testPassingLenientMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testPassingLenientMultipleFilledDiv",
        htmlAssert.div("id", "someid", "class", "someclass").div("class", "someclass")
    );
  }

  @Test
  public void testPassingLenientMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testPassingLenientMultipleMixedDiv",
        htmlAssert.div("id", "someid", "class", "someclass").div()
    );
  }


  @Test
  public void testPassingStrictOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testPassingStrictOrderedFilledDiv",
        htmlAssert.div().div("id", "someid", "class", "someclass")
    );
  }

  @Test
  public void testPassingStrictUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testPassingStrictUnorderedFilledDiv",
        htmlAssert.div().div("class", "someclass", "id", "someid")
    );
  }

  @Test
  public void testPassingStrictMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testPassingStrictMultipleFilledDiv",
        htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass")
    );
  }

  @Test
  public void testPassingStrictMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testPassingStrictMultipleMixedDiv",
        htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass").div()
    );
  }


  @Test(expected = AssertionError.class)
  public void testFailingLenientOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testFailingLenientOrderedFilledDiv",
        htmlAssert.div("id", "someid", "class", "someclass")
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testFailingLenientUnorderedFilledDiv",
        htmlAssert.div("class", "someclass", "id", "someid")
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someNONclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testFailingLenientMultipleFilledDiv",
        htmlAssert.div("id", "someid", "class", "someclass").div("class", "someclass")
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><dZv></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testFailingLenientMultipleMixedDiv",
        htmlAssert.div("id", "someid", "class", "someclass").div()
    );
  }


  @Test(expected = AssertionError.class)
  public void testFailingStrictOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testFailingStrictOrderedFilledDiv",
        htmlAssert.div().div("id", "someid", "class", "someclass")
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testFailingStrictUnorderedFilledDiv",
        htmlAssert.div().div("class", "someclass", "id", "someid")
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someNONclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testFailingStrictMultipleFilledDiv",
        htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass")
    );
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><dZv></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html, Parsing.STRICT);
    it("tests testFailingStrictMultipleMixedDiv",
        htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass").div()
    );
  }


  @Test(expected = AssertionError.class)
  public void testFailingLenientTooManyAttrDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\" hidden><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("tests testFailingLenientTooManyAttrDiv",
        htmlAssert.div("id", "someid", "class", "someclass")
    );
  }


  @Test
  public void testHashMapsEqualWithWildcard() {
    Map<String, String> map1 = new HashMap<String, String>();
    map1.put("key1", "value1");
    map1.put("key2", "val*2");
    map1.put("key3", "val*3 is* value");
    map1.put("key4", "*  (107807613)");

    Map<String, String> map2 = new HashMap<String, String>();
    map2.put("key1", "value1");
    map2.put("key2", "value2");
    map2.put("key3", "value3 is a value");
    map2.put("key4", "1.61e+4k  (107807613)");

    Assert.assertTrue(new TagsFinder().hashMapsAreEqual(map1, map2));
  }

  @Test
  public void testHashMapsNotEqualWithWildcard() {
    Map<String, String> map1 = new HashMap<String, String>();
    map1.put("key1", "value1");
    map1.put("key2", "val*3 is* value");

    Map<String, String> map2 = new HashMap<String, String>();
    map2.put("key1", "value1");
    map2.put("key2", "value2 is a value");

    Assert.assertFalse(new TagsFinder().hashMapsAreEqual(map1, map2));
  }

  @Test
  public void testTextIsPresent() {
    String html = "<div><tr>content</tr></div>";

  }

  @Ignore
  @Test
  public void testTextValue() {
    String html = "<div><tr>content</tr></div>";

    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    htmlAssert.div().text("content");
  }

  @Ignore
  @Test(expected = AssertionError.class)
  public void testTextValueIsHtmlTag() {
    String html = "<div><tr>content</tr></div>";

    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    htmlAssert.div().text("tr");
  }

  @Ignore
  @Test(expected = AssertionError.class)
  public void testTextValueIsAttribute() {
    String html = "<div><tr class=\"someattr\">content</tr></div>";

    HtmlAssertDom htmlAssert = new HtmlAssertDom(html);
    it("test that the text is an attribute", htmlAssert.div().text("someattr"));
  }

  @Test
  public void testProcessTagList() {
    HtmlAssertDom htmlAssert = new HtmlAssertDom("<div><div><tr id=\"id1\"></tr></div></div> <div><div><tr id=\"id2\"></tr></div></div>");
    it("tests deep search",
        htmlAssert.div().div().tr("id", "id2"));
  }

}
