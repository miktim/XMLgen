Usage (Java):
```java
XML xml = new XML(new Node("multistatus xmlns=\"DAV:\""));
xml.setNode("response")
     .addNode("href", "http://www.example.com/container/")
     .setNode("propstat")
       .addNode("status", "HTTP/1.1 200 OK")
       .setNode(Node.tag("prop", "xmlns:R", "http://ns.example.com/schema/"))
         .addNode("R:author", "John Doe")
         .addNode("creationdate", "2026-06-12T23:20:50.52Z")
         .addNode("displayname", "container")
         .addNode("supportedlock");
```  
The xml.toString() method returns the following XML text (actually as a single string):
```xml
<?xml version="1.0" encoding="utf-8" ?>
<multistatus xmlns="DAV:">
  <response>
    <href>http://www.example.com/container/</href>
    <propstat>
      <status>HTTP/1.1 200 OK</status>
      <prop xmlns:R="http://ns.example.com/schema/">
        <R:author>John Doe</R:author>
        <creationdate>2026-06-12T23:20:50.52Z</creationdate>
        <displayname>container</displayname>
        <supportedlock/>
      </prop>
    </propstat>
  </response>
</multistatus>
```  
Package help here: [./README.txt](./README.txt)