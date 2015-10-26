package org.modelcatalogue.letter.annotator;

import java.io.IOException;

/**
 * Letter annotator first collects the candidate terms and than allows to annotate text with given highlighter.
 */
public interface LetterAnnotator {

    /**
     * Adds a new candidate term to the annotator.
     * @param candidateTerm new candidate term
     */
    void addCandidate(CandidateTerm candidateTerm) throws IOException;

    /**
     * Adds a new candidate term to the annotator.
     * @param candidateTerm new candidate term
     */
    void addCandidate(CandidateTerm.Builder candidateTerm) throws IOException;

    /**
     * Annotates the letter.
     * @param letter the letter to be annotated
     * @param highlighter the highlighter to be used for highlighting the matches
     * @return the annotated letter with the letter text, collection of candidate terms which may be present in the letter
     */
    AnnotatedLetter annotate(CharSequence letter, Highlighter highlighter) throws IOException;

}
