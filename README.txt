XMLgen - The simplest XML generator, MIT (c) 2026 miktim@mail.ru

This is an attempt to implement the simplest XML generator.

The jar ./dist/xmlgen-... file was generated with debugging info
using JDK1.8 for target JRE1.7

package org.miktim.xmlgen;

class XML extends Node;
  Constructor:
    XML(Node rootNode);
      Creates an XML with the root node.

  Constant:
    static String NAME_PATTERN;
      - the regex XML names pattern: [prefix:]name
  Methods:
    String toString();
      - returns XML text encoded in the JVM's DEFAULT charset.
    String toString(String charset) throws UnsupportedEncodingException;
      - returns XML text encoded in the specified charset
    void toStream(OutputStream out) throws IOException;
      - writes XML text, encoded in the JVM's DEFAULT charset,
        to the output stream and closes this stream.
    void toStream(OutputStream out, String charset) throws IOException;
      - writes XML text, encoded in the specified charset,
        to the output stream and closes this stream.

  Note:
    In Java 17 and earlier, the default charset was dynamic;
    it was determined at startup based on the user's operating system,
    locale, and regional settings (e.g., windows-1252 on Western Windows).

class Node;
  Node is XML element node.

  Constructors:
    Node(String tag);
      Creates a node.
      - the tag is a node name with an optional prefix and optional attributes;
      - use the Node.tag method to escape attribute values;
      - checks the tag syntax.
        Example:
          new Node("prop xmlns:R=\"http://ns.example.com/schema/\"");
    Node(String tag, Object content);
      Creates a text node.
      - escapes ("<", ">", "&") the text (String instance) content;
      - converts the content object into its String representation;
      - the content can be null.
        Examples:
          new Node("R:author", "John Doe");
          new Node("IsReadOnly xmlns=\"http://ucb.openoffice.org/dav/props/\"",false);

    Throws:
      NullPointerException: when the tag is null;
      IllegalArgumentException: when the tag is empty or syntactically incorrect.

  Methods:
    Node setNode(Node node);
      - adds the node to the this (current parent) Node;
      - avoid recursion!;
      - returns node (new parent node).
    Node addNode(Node node);
      - adds the node to the this (current parent) Node.
      - avoid recursion!;
      - returns this (current parent node).
    Node setNode(String tag);
      - adds new node to the parent Node;
      - returns new node.
    Node addNode(String tag);
      - adds new node to the parent Node;
      - returns this.
    Node setNode(String tag, Object content);
      - adds new node to the parent Node;
      - returns new node.
    Node addNode(String tag, Object content);
      - adds new node to the parent Node;
      - returns this.

    Throws:
      NullPointerException: when the node or tag argument is null;
      IllegalArgumentException: when the tag is empty or syntactically incorrect.

    static String tag(String tagName, String... attrs);
      The tag "constructor".
      - attrs are attribute name-value pairs;
      - the attribute values will be escaped ("\"","<",">","&")
        and enclosed in double quotes;
      - returns the checked tag. 
      Throws: 
        ArrayIndexOutOfBoundsException: when the name-value pair is incomplete.
        NullPointerException: when the name or value is null;
        IllegalArgumentException: when the tag is empty or syntactically incorrect.
      
    static String escape(String value);
      - escapes "<", ">", "&".
    String toString();
      - returns XML text as a single line.

Example:

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

xml.toString() returns following XML text (actually as a single line):

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
