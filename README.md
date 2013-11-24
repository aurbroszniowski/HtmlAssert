HtmlAssert
==========

DSL based assertion API to assert HTML path and content

Given a HTML String, this API is made to help you asserting the content of the HTML

 e.g.<br/>
  ```
  &lt;table id="mytable"&gt;<br/>
    &lt;tr class="someclass"&gt;<br/>
      &lt;td class="someclass" id="sometd"&gt;content&lt;/td&gt;<br/>
    &lt;/tr&gt;<br/>
  &lt;/table&gt;<br/>
  ```

  Will be asserted by <br/>
  ```
  String html = ... // content above <br/>
  HtmlAssert htmlAssert = new HtmlAssert(html); <br/>
  htmlAssert.table("id","mytable").tr("class","someclass").td("class","someclass", "id","sometd").text("content"); <br/>
  ```

 You have two modes : LENIENT and STRICT <br/>
  ```
  HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.LENIENT); <br/>
  HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT); <br/>
  ```

 In LENIENT mode, all parameters are optional, in case they are blank, HtmlAssert will try to find itself the path, <br/>
  e.g. the following will work similarly: <br/>
  ```
  htmlAssert.table().tr().td().text("content"); <br/>
  htmlAssert.text("content"); <br/>
  ```

 In STRICT mode, the whole path must be defined, <br/>
  e.g. the following will work: <br/>
  ```
  htmlAssert.table("id","mytable").tr("class","someclass").td("class","someclass", "id","sometd").text("content"); <br/>
  ```
  but not this, because it is missing the "table" tag <br/>
  ```
  htmlAssert.tr("class","someclass").td("class","someclass", "id","sometd").text("content"); <br/>
  ```

