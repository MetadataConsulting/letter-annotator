package org.modelcatalogue.letter.annotator;

/**
 * Highlights the matched text from the letter using the matched term.
 */
public interface Highlighter {

    /**
     * The highlighter which doesn't do anything.
     */
    Highlighter NOOP = new AbstractHighlighter() {
        @Override
        public String getReplacement(String originalMatch, CandidateTerm matchedTerm) {
            return originalMatch;
        }
    };


    /**
     * The highlighter which highlights the text with HTML fragments.
     */
    Highlighter HTML = new HtmlHighlighter();

    /**
     * Returns the custom head for the highlighter which should be appended to the highlighted text.
     * @return the custom head for the highlighter which should be appended to the highlighted text
     */
    String getHead();

    /**
     * Returns the custom tail for the highlighter which should be appended to the highlighted text.
     * @return the custom tail for the highlighter which should be appended to the highlighted text
     */
    String getTail();

    /**
     * Returns the match from the fragments with more data encoded using the information from the matched term.
     * @param originalMatch matched text
     * @param matchedTerm related term
     * @return the match from the fragments with more data encoded using the information from the matched term
     */
    String getReplacement(String originalMatch, CandidateTerm matchedTerm);

}
