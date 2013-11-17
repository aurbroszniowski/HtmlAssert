package com.jsoft;


import org.junit.Test;

/**
 *   //TODO: try static attributes ?
 *
 * @author Aurelien Broszniowski
 */
public class HtmlAssertTest {

  @Test
  public void testPassingDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\" hidden><div class=\"someclass\" ><div><table></table></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);

    htmlAssert.div("id", "someid", "class", "someclass", "hidden", null).div("class", "someclass").table();
    htmlAssert.div("hidden", null, "class", "someclass", "id", "someid");
  }


  // Empty Div tests

  @Test
  public void testPassingLenientEmptyDiv() {
    String html = "<dZv><div></div></dZv>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div();
  }

  @Test
  public void testPassingStrictEmptyDiv() {
    String html = "<div><div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div();
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientEmptyDiv() {
    String html = "<dZv></dZv>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.LENIENT);
    htmlAssert.div();
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictEmptyDiv() {
    String html = "<dZv><div></div></dZv>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div();
  }

  // Filled Div tests

  @Test
  public void testPassingLenientOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass");
  }

  @Test
  public void testPassingLenientUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("class", "someclass", "id", "someid");
  }

  @Test
  public void testPassingLenientMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass").div("class", "someclass");
  }

  @Test
  public void testPassingLenientMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass").div();
  }


  @Test
  public void testPassingStrictOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("id", "someid", "class", "someclass");
  }

  @Test
  public void testPassingStrictUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("class", "someclass", "id", "someid");
  }

  @Test
  public void testPassingStrictMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass");
  }

  @Test
  public void testPassingStrictMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass").div();
  }


  @Test(expected = AssertionError.class)
  public void testFailingLenientOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass");
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("class", "someclass", "id", "someid");
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someNONclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass").div("class", "someclass");
  }

  @Test(expected = AssertionError.class)
  public void testFailingLenientMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><dZv></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass").div();
  }


  @Test(expected = AssertionError.class)
  public void testFailingStrictOrderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("id", "someid", "class", "someclass");
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictUnorderedFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someNONclass\"><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("class", "someclass", "id", "someid");
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictMultipleFilledDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someNONclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass");
  }

  @Test(expected = AssertionError.class)
  public void testFailingStrictMultipleMixedDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\"><div class=\"someclass\" ><dZv></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
    htmlAssert.div().div("id", "someid", "class", "someclass").div("class", "someclass").div();
  }


  @Test(expected = AssertionError.class)
  public void testFailingLenientTooManyAttrDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\" hidden><div class=\"someclass\" ><div></div></div></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div("id", "someid", "class", "someclass");
  }

}
