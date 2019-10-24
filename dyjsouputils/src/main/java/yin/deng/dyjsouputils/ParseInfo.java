package yin.deng.dyjsouputils;

import android.sax.Element;

import org.jsoup.select.Elements;

import java.io.Serializable;

public class ParseInfo implements Serializable {
    private Element element;
    private Elements elements;
    private boolean isEmpty=false;

    public ParseInfo(Elements elements, boolean isEmpty) {
        this.elements = elements;
        this.isEmpty = isEmpty;
    }

    public ParseInfo() {
    }

    public ParseInfo(Element element, boolean isEmpty) {
        this.element = element;
        this.isEmpty = isEmpty;
    }

    public ParseInfo(Element element) {
        this.element = element;
    }

    public ParseInfo(Elements elements) {
        this.elements = elements;
    }

    public Element getElement() {
        return element;
    }

    public ParseInfo(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
        this.elements = elements;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
