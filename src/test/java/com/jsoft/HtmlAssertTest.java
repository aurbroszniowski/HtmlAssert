package com.jsoft;


import org.junit.Test;

import static com.jsoft.attributes.HtmlAttribute.$;
import static com.jsoft.attributes.HtmlAttribute.attributes;

/**
 * @author Aurelien Broszniowski
 */
public class HtmlAssertTest {

  @Test
  public void testPassingDiv() {
    String html = "<div><div id=\"someid\" class=\"someclass\" hidden></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div();
  }

/*

  @Test
  public void testPassingLenientDivWithAttributes() {
    String html = "<div><div id=\"someid\" lang=\"fr\" hidden></div></div>";
    HtmlAssert htmlAssert = new HtmlAssert(html);
    htmlAssert.div().id("someid").lang("fr");

//    TODO : implement this syntax in simple cases then complex cases (that one)
    HtmlAssert.validate(
        HtmlAssert.html(html)
            .div().id("someid").lang("fr")
            .div()
            .div().id("someid").lang("fr")
    );


        .div($().id("someid").lang("fr").$());
  }
*/

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
}
