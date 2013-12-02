package com.jsoft;

/**
 * @author Aurelien Broszniowski
 */
public class Tag {

  private String tag;
  private String[] attributes;

  public Tag(final String tag, final String[] attributes) {
    this.tag = tag;
    this.attributes = attributes;
  }

  public Tag(final String tag) {
    this(tag, new String[] { });
  }

  public String getTag() {
    return tag;
  }

  public String[] getAttributes() {
    return attributes;
  }
}
