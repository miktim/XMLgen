/*
 * XMLgen XML, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import static java.lang.String.format;
import java.nio.charset.Charset;

public class XML extends Node {

    public XML(Node rootNode) {
        nodeTag = rootNode.nodeTag;
        nodeList = rootNode.nodeList;
    }

    public static final String NAME_PATTERN
            = "([_\\p{L}][._\\p{L}0-9]*)(:([_\\p{L}][._\\p{L}0-9]*))*";

    @Override
    public String toString() {
        return getDeclaration(Charset.defaultCharset().toString())
                + super.toString();
    }

    public String toString(String charset) throws UnsupportedEncodingException {
        String xml = getDeclaration(charset) + super.toString();
        return new String(xml.getBytes(charset));
    }

    public void toStream(OutputStream out) throws IOException {
        toStream(out, Charset.defaultCharset().toString());
    }

    public void toStream(OutputStream out, String charset) throws IOException {
        byte[] bytes
                = (getDeclaration(charset) + super.toString()).getBytes(charset);
        out.write(bytes);
        out.flush();
        out.close();
    }

    private String getDeclaration(String charset) {
        return format("<?xml version=\"1.0\" encoding=\"%s\"?>\n", charset);
    }
}
