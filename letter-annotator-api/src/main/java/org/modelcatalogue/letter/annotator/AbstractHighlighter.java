package org.modelcatalogue.letter.annotator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of Highlighter which handles some syntactic sugar methods.
 */
public abstract class AbstractHighlighter implements Highlighter {

    protected abstract TextWithOccurrences highlight(String letter, Map<String, CandidateTerm> candidateTerms);

    @Override
    public final TextWithOccurrences highlight(String letter, Collection<CandidateTerm> candidateTerms) {
        if (candidateTerms.isEmpty()) {
            return new TextWithOccurrences(letter, TermOccurrence.collect().getOccurrences());
        }

        Map<String, CandidateTerm> candidateTermsMap = new HashMap<String, CandidateTerm>();

        for (CandidateTerm term : candidateTerms) {
            candidateTermsMap.put(term.getTerm(), term);
            for (String synonym : term.getSynonyms()) {
                candidateTermsMap.put(synonym, term);
            }
        }

        return highlight(letter, candidateTermsMap);
    }

    @Override
    public final TextWithOccurrences highlight(String letter, CandidateTerm... candidateTerms) {
        return highlight(letter, Arrays.asList(candidateTerms));
    }
}
