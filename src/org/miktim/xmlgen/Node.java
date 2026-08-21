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

    public Node(String tag) {
        tag = tag.trim();
        checkTag(tag);
        nodeTag = tag;
    }

    public Node(String tag, Object content) {
        this(tag);
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

    private static void checkTag(String tag) {
// rough syntax check
        String tagPattern = format("^%s(\\s+%1$s=\"[^\"]*\")*$", XML.NAME_PATTERN);
        if (tag.matches(tagPattern)) {
            return;
        }
        throw new IllegalArgumentException("tag: " + tag);
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

    public Node setNode(String tag) {
        Node node = new Node(tag);
        return setNode(node);
    }

    public Node addNode(String tag) {
        Node node = new Node(tag);
        return addNode(node);
    }

    public Node addNode(String tag, Object content) {
        Node node = new Node(tag, content);
        return addNode(node);
    }

    public Node setNode(String tag, Object content) {
        Node node = new Node(tag, content);
        return setNode(node);
    }

    public static String escape(String value) {
        return value
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String endTag = "";
//        if (hasTag()) { // root?
            if (nodeList.isEmpty()) {
                sb.append(format("<%s/>", nodeTag));
            } else {
                sb.append(format("<%s>", escape(nodeTag)));
                endTag = format("</%s>", nodeTag.split(" ", 2)[0]);
            }
//        }
        for (Object node : nodeList) {
            sb.append(node.toString());
        }
        sb.append(endTag);
        return sb.toString();
    }
}
