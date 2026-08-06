/*
 * XMLgen XML, MIT (c) 2026 miktim@mail.ru
 */
package org.miktim.xmlgen;

public class XML extends Node {

    public XML() {
    }

    @Override
    public String toString() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n"
                + super.toString();
    }
}
