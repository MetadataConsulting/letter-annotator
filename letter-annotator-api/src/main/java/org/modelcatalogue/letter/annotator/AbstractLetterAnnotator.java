package org.modelcatalogue.letter.annotator;


import java.util.Collection;

/**
 * Base class for LetterAnnotator which helps to handle syntactic sugar methods.
 */
public abstract class AbstractLetterAnnotator implements LetterAnnotator {

    private Highlighter highlighter = Highlighter.HTML;
    private TermMatcher termMatcher = TermMatcher.PATTERN_MATCHER;
    private Scorer scorer = Scorer.POSITIVE_ONLY;

    protected abstract Collection<CandidateTerm> getCandidateTerms(CharSequence letter);

    @Override
    public final AnnotatedLetter annotate(CharSequence letter) {
        return getTermMatcher().annotate(getHighlighter(), getScorer(), letter.toString(), getCandidateTerms(letter));
    }

    @Override
    public final void addCandidate(CandidateTerm.Builder candidateTerm) {
        addCandidate(candidateTerm.build());
    }

    @Override
    public final void setHighlighter(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    @Override
    public final void setTermMatcher(TermMatcher termMatcher) {
        this.termMatcher = termMatcher;
    }

    protected final Highlighter getHighlighter() {
        return highlighter;
    }

    protected final TermMatcher getTermMatcher() {
        return termMatcher;
    }

    protected Scorer getScorer() {
        return scorer;
    }

    @Override
    public void setScorer(Scorer scorer) {
        this.scorer = scorer;
    }
}
