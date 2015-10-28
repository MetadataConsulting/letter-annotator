package org.modelcatalogue.letter.annotator;

import java.util.Collection;

public interface TermMatcher {

    TermMatcher PATTERN_MATCHER = new PatternMatcher();

    AnnotatedLetter annotate(Highlighter highlighter, Scorer scorer, CharSequence letter, Collection<CandidateTerm> candidateTerms);
    AnnotatedLetter annotate(Highlighter highlighter, Scorer scorer, CharSequence letter, CandidateTerm... candidateTerms);

}
