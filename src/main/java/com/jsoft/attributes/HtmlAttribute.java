package com.jsoft.attributes;

import java.util.HashMap;

/**
 * @author Aurelien Broszniowski
 */
public class HtmlAttribute {

  private HashMap<String, String> attributes = new HashMap<String, String>();

  public HtmlAttribute() {
  }

  public static HtmlAttribute attributes() {
    return new HtmlAttribute();
  }

  public static HtmlAttribute $() {
    return new HtmlAttribute();
  }

  public HtmlAttribute id(String value) {
    attributes.put("id", value);
    return this;
  }
}
