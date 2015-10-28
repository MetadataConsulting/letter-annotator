package org.modelcatalogue.letter.annotator;

/**
 * Scores the matched term.
 */
public interface Scorer {

    Scorer POSITIVE_ONLY = new PositiveOnlyScorer();

    /**
     * Scores the found term in the text.
     * @param collector the collector of term statistics
     * @param letter the whole text of the letter
     * @param matchedText the part of the letter matched
     * @param startPosition start position of the match
     * @param endPosition end position of the match
     * @param term the matched term
     */
    void score(TermOccurrence.Collector collector, String letter, String matchedText, int startPosition, int endPosition, CandidateTerm term);
}
