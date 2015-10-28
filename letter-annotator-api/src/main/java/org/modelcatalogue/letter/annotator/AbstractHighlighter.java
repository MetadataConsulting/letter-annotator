package org.modelcatalogue.letter.annotator;

/**
 * Abstract implementation of Highlighter which allows to set custom head and tail.
 */
public abstract class AbstractHighlighter implements Highlighter {

    private String head = "";
    private String tail = "";


    /**
     * Sets the custom head for the highlighter which should be prepended to the highlighted text.
     * @param head the custom head for the highlighter which should be prepended to the highlighted text
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * Sets the custom tail for the highlighter which should be appended to the highlighted text.
     * @param tail the custom tail for the highlighter which should be appended to the highlighted text
     */
    public void setTail(String tail) {
        this.tail = tail;
    }

    @Override
    public String getTail() {
        return tail;
    }

    @Override
    public String getHead() {
        return head;
    }
}
