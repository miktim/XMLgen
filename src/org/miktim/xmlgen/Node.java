/*
 * XMLgen Node, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Node {

    String nodeName = null;
    ArrayList<Object> nodeList = new ArrayList<>(); // not Thread-safe

    public Node() {
    }

    public Node(String tag) {
        tag = tag.trim();
        checkTag(tag);
        nodeName = tag;
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

    public boolean isEmpty() {
        return nodeList.isEmpty();
    }

    boolean hasName() {
        return nodeName != null;
    }

    private static void checkTag(String tag) {
// rough syntax check
        String tagPattern = format("^%s(\\s+%1$s=\"[^\"]*\")*$", XML.NAME_PATTERN);
        if (tag.matches(tagPattern)) {
            return;
        }
        throw new IllegalArgumentException("tag: " + tag);
    }

    public Node setNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        if (node.hasName()) {
            nodeList.add(node);
        }
        return node.hasName() ? node : this;
    }

    public Node addNode(Node node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        if (!hasName() && node.hasName()) {
            throw new NoSuchElementException("Orphan. No parent");
        }
        if (node.hasName()) {
            nodeList.add(node);
        }
        return this;
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
        if (hasName()) { // root?
            if (nodeList.isEmpty()) {
                sb.append(format("<%s/>", nodeName));
            } else {
                sb.append(format("<%s>", escape(nodeName)));
                endTag = format("</%s>", nodeName.split(" ", 2)[0]);
            }
        }
        for (Object node : nodeList) {
            sb.append(node.toString());
        }
        sb.append(endTag);
        return sb.toString();
    }
}
