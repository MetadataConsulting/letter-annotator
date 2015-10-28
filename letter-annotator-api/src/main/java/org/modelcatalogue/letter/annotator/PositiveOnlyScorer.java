package org.modelcatalogue.letter.annotator;

/**
 * Scorer which scores every match positively.
 */
public final class PositiveOnlyScorer implements Scorer {

    @Override
    public void score(TermOccurrence.Collector collector, String letter, String matchedText, int startPosition, int endPosition, CandidateTerm term) {
        collector.increment(term);
    }
}
