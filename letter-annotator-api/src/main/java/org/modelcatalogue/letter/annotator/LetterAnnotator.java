package org.modelcatalogue.letter.annotator;

/**
 * Letter annotator first collects the candidate terms and than allows to annotate text with given highlighter.
 *
 * Classes implementing this interface are stateful by nature and they are not expected to be thread safe.
 */
public interface LetterAnnotator {

    /**
     * Adds a new candidate term to the annotator.
     * @param candidateTerm new candidate term
     */
    void addCandidate(CandidateTerm candidateTerm);

    /**
     * Adds a new candidate term to the annotator.
     * @param candidateTerm new candidate term
     */
    void addCandidate(CandidateTerm.Builder candidateTerm);

    /**
     * Sets the highlighter to be used in all subsequent call of #annotate(CharSequence) method.
     * @param highlighter the highlighter to be used in all subsequent call of #annotate(CharSequence) method
     */
    void setHighlighter(Highlighter highlighter);


    /**
     * Sets the matcher to be used in all subsequent call of #annotate(CharSequence) method.
     * @param matcher the matcher to be used in all subsequent call of #annotate(CharSequence) method
     */
    void setTermMatcher(TermMatcher matcher);

    /**
     * Sets the scorer to be used in all subsequent call of #annotate(CharSequence) method.
     * @param scorer the scorer to be used in all subsequent call of #annotate(CharSequence) method
     */
    void setScorer(Scorer scorer);

    /**
     * Annotates the letter.
     * @param letter the letter to be annotated
     * @return the annotated letter with the letter text, collection of candidate terms which may be present in the letter
     */
    AnnotatedLetter annotate(CharSequence letter);

}
