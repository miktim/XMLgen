/*
 * XMLgen Node, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

import static java.lang.String.format;
import java.util.ArrayList;

public class Node {

    String nodeTag = null;
    ArrayList<Object> nodeList = new ArrayList<>(); // not Thread-safe

    protected Node() {

    }

    public Node(String nodeName) {
        this.nodeTag = checkName(nodeName.trim());
    }

    public Node(String nodeName, Object content) {
        this(nodeName);
        if (content == null) {
            return;
        }
        if (content instanceof String) {
            if (((String) content).isEmpty()) {
                return;
            }
            content = escape((String) content);
        }
        nodeList.add(String.valueOf(content));
    }

    public final Node setNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node = dereferenceXml(node);
        nodeList.add(node);
        return node;
    }

    public Node addNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node = dereferenceXml(node);
        nodeList.add(node);
        return this;
    }

    static Node dereferenceXml(Node node) {
        if (node instanceof XML) {
            Node newNode = new Node(node.nodeTag);
            newNode.nodeList = node.nodeList;
            return newNode;
        }
        return node;
    }
    
    private static String checkName(String name) {
        if (name.matches(XML.NAME_PATTERN)) {
            return name;
        }
        throw new IllegalArgumentException("illegal name: " + name);
    }
    
    public Node addAttr(String attrName, String value) {
            nodeTag += format(" %s=\"%s\"",
                    checkAttr(attrName.trim()),
                    escape(value).replaceAll("\"", "&quot;"));
        return this;
    }
    
    public Node addAttr(String... attrs) {
        for (int i = 0; i < attrs.length; i++) {
            addAttr(attrs[i], attrs[++i]);
        }
        return this;
    }

    private String checkAttr(String attrName) {
        attrName = checkName(attrName);
        if(!nodeTag.matches(format(" %s=", attrName)))
            return attrName;
        throw new IllegalArgumentException("duplicate attr: " + attrName);
    }

    public Node setNode(String nodeName) {
        Node node = new Node(nodeName);
        return setNode(node);
    }

    public Node addNode(String nodeName) {
        Node node = new Node(nodeName);
        return addNode(node);
    }

    public Node addNode(String nodeName, Object content) {
        Node node = new Node(nodeName, content);
        return addNode(node);
    }

    public Node setNode(String nodeName, Object content) {
        Node node = new Node(nodeName, content);
        return setNode(node);
    }

    public static String escape(String value) {
        return value
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String endTag = "";
        if (nodeList.isEmpty()) {
            sb.append(format("<%s/>", nodeTag));
        } else {
            sb.append(format("<%s>", nodeTag));
            endTag = format("</%s>", nodeTag.split(" ", 2)[0]);
        }
        for (Object node : nodeList) {
            sb.append(node.toString());
        }
        sb.append(endTag);
        return sb.toString();
    }
}
