/*
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.miktim.xmlgen.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.miktim.xmlgen.XML;
import org.xml.sax.Attributes;

public class TestXMLgen {

    static void log(Object obj) {
        System.out.println(obj);
    }

    static void checkXml(String xml, boolean show) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        DefaultHandler handler = new DefaultHandler();
        if (show) {
            handler = new DefaultHandler() {
                String node = "";

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) {
                    node = "start: " + qName + " " + uri;
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    log(node += " content: " + new String(ch, start, length).trim());
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    log("end: " + qName + " " + uri);
                }
            };
        }
        parser.parse(new ByteArrayInputStream(xml.getBytes()), handler);

    }

    public static void main(String[] args) throws Exception {
        XML xml = new XML();
        xml.setNode("multistatus xmlns=\"DAV:\"")
                .setNode("response")
                .addNode("href", "http://www.example.com/container/")
                .setNode("propstat")
                .addNode("status", "HTTP/1.1 200 OK")
                .setNode("prop xmlns:R=\"http://ns.example.com/schema/\"")
                .addNode("R:author", "John Doe")
                .addNode("creationdate", "2026-06-12T23:20:50.52Z")
                .addNode("displayname", "container")
                .addNode("supportedlock", null);
        log(xml + "\r\n");
        checkXml(xml.toString(), true);
        log("Ok");//CRLF
        xml = new XML();
//        checkXml(xml.toString(), false); // SAXParseException on empty xml
        xml.addNode("TextNode", "text")
                .addNode("ChildNode");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
        xml = new XML();
        xml.addNode(new Node())  // "null" node as root node
                .setNode(new Node())
                .addNode("TextNode", "text")
                .addNode("ChildNode");
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
        xml = new XML();
        Node node = new Node("TextNode", "text");
        node.addNode(new Node("ChildTextNode", "child text"));
        xml.addNode(node);
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
        xml = new XML();
        node = new Node("ParentNode");
        node.addNode(new Node()) // "null" node between
                .addNode("ChildNode");
        xml.addNode(node);
        log(xml);
        checkXml(xml.toString(), false);
        log("Ok");
    }
}
