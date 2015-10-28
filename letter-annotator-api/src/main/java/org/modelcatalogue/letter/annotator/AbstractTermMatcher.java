package org.modelcatalogue.letter.annotator;

import java.util.*;

/**
 * Abstract base for term matchers handles the syntactic sugar methods.
 */
public abstract class AbstractTermMatcher implements TermMatcher {

    protected abstract AnnotatedLetter annotate(Highlighter highlighter, Scorer scorer, String letter, Map<String, CandidateTerm> candidateTerms);

    public final AnnotatedLetter annotate(Highlighter highlighter, Scorer scorer, CharSequence letter, Collection<CandidateTerm> candidateTerms) {
        if (candidateTerms.isEmpty()) {
            return new AnnotatedLetter(letter.toString(), TermOccurrence.collect().getOccurrences());
        }

        Map<String, CandidateTerm> candidateTermsMap = new HashMap<String, CandidateTerm>();

        for (CandidateTerm term : candidateTerms) {
            candidateTermsMap.put(term.getTerm(), term);
            for (String synonym : term.getSynonyms()) {
                candidateTermsMap.put(synonym, term);
            }
        }

        return annotate(highlighter, scorer, letter.toString(), candidateTermsMap);
    }

    public final AnnotatedLetter annotate(Highlighter highlighter, Scorer scorer, CharSequence letter, CandidateTerm... candidateTerms) {
        return annotate(highlighter, scorer, letter.toString(), Arrays.asList(candidateTerms));
    }

    protected final AnnotatedLetter nothingToHighlight(Highlighter highlighter, String letter) {
        return new AnnotatedLetter(highlighter.getHead() + letter + highlighter.getTail(), Collections.<TermOccurrence>emptySet());
    }

}
