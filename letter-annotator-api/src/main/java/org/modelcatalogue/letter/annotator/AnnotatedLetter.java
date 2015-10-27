package org.modelcatalogue.letter.annotator;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Annotated letter contains the original letter and the set of candidate terms which may or may not occur in
 * the letter. To get the letter annotated with the supplied highlighter use #getHighlighted() method.
 */
public final class AnnotatedLetter {

    private final Collection<CandidateTerm> candidateTerms;
    private final String letter;
    private final Highlighter highlighter;

    private TextWithOccurrences textWithOccurrences;

    /**
     * Creates new annotated letter with no-op Highlighter.
     *
     * @param letter the letter to be annotated
     * @param candidateTerms the candidate terms
     */
    public AnnotatedLetter(String letter, Collection<CandidateTerm> candidateTerms) {
        this(letter, candidateTerms, Highlighter.NOOP);
    }

    /**
     * Creates new annotated letter with given Highlighter.
     *
     * @param letter the letter to be annotated
     * @param candidateTerms the candidate terms
     * @param highlighter the highlighter used by #getHighlighted() method
     */
    public AnnotatedLetter(String letter, Collection<CandidateTerm> candidateTerms, Highlighter highlighter) {
        if (candidateTerms == null) {
            throw new IllegalArgumentException("Candidate terms cannot be null");
        }
        if (letter == null) {
            throw new IllegalArgumentException("Letter cannot be null");
        }
        if (highlighter == null) {
            this.highlighter = Highlighter.NOOP;
        } else {
            this.highlighter = highlighter;
        }
        this.candidateTerms = Collections.unmodifiableCollection(candidateTerms);
        this.letter = letter;
    }

    /**
     * @return all candidate terms for this letter
     */
    public Collection<CandidateTerm> getCandidateTerms() {
        return candidateTerms;
    }

    /**
     * @return original letter
     */
    public String getLetter() {
        return letter;
    }

    /**
     * @return the letter highlighted by the highlighter supplied in the constructor
     */
    public String getHighlighted() {
        if (textWithOccurrences == null) {
            textWithOccurrences = highlighter.highlight(letter, candidateTerms);
        }
        return textWithOccurrences.getText();
    }

    public Set<TermOccurrence> getOccurrences() {
        if (textWithOccurrences == null) {
            textWithOccurrences = highlighter.highlight(letter, candidateTerms);
        }
        return textWithOccurrences.getOccurrences();
    }

}
