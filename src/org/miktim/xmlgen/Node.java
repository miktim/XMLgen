/*
 * XMLgen Node, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

import static java.lang.String.format;
import java.util.ArrayList;

public class Node {

    String nodeName = null;
    final ArrayList<Object> nodeList = new ArrayList<>();

    public Node() {
    }

    public Node(String tag) {
        tag = tag.trim();
        if (tag.isEmpty()) {
            throw new IllegalArgumentException("nodeName");
        }
        nodeName = tag;
    }

    public Node(String tag, Object content) {
        this(tag);
        if (content == null) {
            return;
        }
        if (content instanceof String) {
            content = escape((String) content);
        }
        nodeList.add(String.valueOf(content));
    }
    public boolean isEmpty() {
        return nodeList.isEmpty();
    }
    private boolean hasName() {
        return nodeName != null;
    }
    
    public Node setNode(Node node) {
        if(node == null) throw new NullPointerException("node");
        nodeList.add(node);
        return node;
    }
    public Node addNode(Node node) {
        if(node == null) throw new NullPointerException("node");
        nodeList.add(node);
        return hasName() ? this : node;
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
        for (Object node : nodeList) {
            sb.append(node.toString());
        }
        if (hasName()) { // root?
            if (nodeList.isEmpty()) {
                sb.append(format("<%s/>", nodeName));
            } else {
                sb.insert(0, format("<%s>", escape(nodeName)));
                sb.insert(sb.length(), format("</%s>", nodeName.split(" ", 2)[0]));
            }
        }
        return sb.toString();
    }
}
