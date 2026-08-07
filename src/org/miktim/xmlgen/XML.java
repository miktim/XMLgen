/*
 * XMLgen XML, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

import static java.lang.String.format;
import java.nio.charset.Charset;

public class XML extends Node {

    public XML() {
    }

    @Override
    public String toString() {
        return format("<?xml version=\"1.0\" encoding=\"%s\" ?>\n", Charset.defaultCharset())
                + super.toString();
    }
}
