HtmlAssert
==========

DSL based assertion API to assert HTML path and content


Installation
------------

  ```
  mvn install
  ```

  ```
  <dependency>
    <groupId>com.jsoft</groupId>
     <artifactId>htmlassert</artifactId>
     <version>1.0-SNAPSHOT</version>
  </dependency>
  ```

Usage
-----

Given a HTML String, this API is made to help you asserting the content of the HTML

 e.g.<br/>
  ```
  <table id="mytable">
    <tr class="someclass">
      <td class="someclass" id="sometd">content</td>
    </tr>
  </table>
  ```

  Will be asserted by <br/>
  ```
  String html = ... // content above
  HtmlAssert htmlAssert = new HtmlAssert(html);
  htmlAssert.table("id","mytable")
            .tr("class","someclass")
            .td("class","someclass", "id","sometd")
            .text("content");
  ```

 You have two modes : LENIENT and STRICT <br/>
  ```
  HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.LENIENT);
  HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);
  ```

 In LENIENT mode, all parameters are optional, in case they are blank, HtmlAssert will try to find itself the path, <br/>
  e.g. the following will work similarly: <br/>
  ```
  htmlAssert.table().tr().td().text("content");
  htmlAssert.text("content");
  ```

 In STRICT mode, the whole path must be defined, <br/>
  e.g. the following will work: <br/>
  ```
  htmlAssert.table("id","mytable")
            .tr("class","someclass")
            .td("class","someclass", "id","sometd")
            .text("content");
  ```
  but not this, because it is missing the "table" tag <br/>
  ```
  htmlAssert.tr("class","someclass")
            .td("class","someclass", "id","sometd")
            .text("content");
  ```

