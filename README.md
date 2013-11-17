HtmlAssert
==========

DSL based assertion API to assert HTML path and content

Given a HTML String, this API is made to help you asserting the content of the HTML

 e.g.
  &lt;table id="mytable"&gt;
    &lt;tr class="someclass"&gt;
      &lt;td class="someclass" id="sometd"&gt;content&lt;/td&gt;
    &lt;/tr&gt;
  &lt;/table&gt;

  Will be asserted by
  String html = ... // content above
  HtmlAssert htmlAssert = new HtmlAssert(html);
  htmlAssert.table("id","mytable").tr("class","someclass").td("class","someclass", "id","sometd").text("content");

 You have two modes : LENIENT and STRICT
  HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.LENIENT);
  HtmlAssert htmlAssert = new HtmlAssert(html, Parsing.STRICT);

 In LENIENT mode, all parameters are optional, in case they are blank, HtmlAssert will try to find itself the path,
  e.g. the following will work similarly:
  htmlAssert.table().tr().td().text("content");
  htmlAssert.text("content");

 In STRICT mode, the whole path must be defined,
  e.g. the following will work:
  htmlAssert.table("id","mytable").tr("class","someclass").td("class","someclass", "id","sometd").text("content");
  but not this, because it is missing the "table" tag
  htmlAssert.tr("class","someclass").td("class","someclass", "id","sometd").text("content");

