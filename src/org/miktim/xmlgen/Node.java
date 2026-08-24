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
        nodeTag = checkTag(tag.trim());
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

    static String TAG_PATTERN
            = format("^%s(\\s+%1$s=((\"[^\"]*\")|('[^']*')))*$", XML.NAME_PATTERN);

    private static String checkTag(String tag) {
// rough syntax check
        if (tag.matches(TAG_PATTERN)) {
            return tag;
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
    
    public static String tag(String tagName, String... attr) {
        String tag = checkTag(tagName);
        for(int i = 0; i < attr.length; i++) {
            tag += format(" %s=\"%s\"",
                    attr[i], escape(attr[++i]).replaceAll("\"", "&quote;"));
        }
        return checkTag(tag);
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
            sb.append(format("<%s>", escape(nodeTag)));
            endTag = format("</%s>", nodeTag.split(" ", 2)[0]);
        }
        for (Object node : nodeList) {
            sb.append(node.toString());
        }
        sb.append(endTag);
        return sb.toString();
    }
}
