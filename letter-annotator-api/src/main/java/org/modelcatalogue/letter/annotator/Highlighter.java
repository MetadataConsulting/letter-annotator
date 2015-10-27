package org.modelcatalogue.letter.annotator;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Highlights the letter using the candidate terms.
 */
public interface Highlighter {

    Highlighter NOOP = new AbstractHighlighter() {
        @Override
        protected TextWithOccurrences highlight(String letter, Map<String, CandidateTerm> candidateTerms) {
            return new TextWithOccurrences(letter, TermOccurrence.collect().getOccurrences());
        }
    };

    Highlighter HTML = new HtmlHighlighter();

    int HIGHLIGHTER_PATTERN_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;

    TextWithOccurrences highlight(String letter, Collection<CandidateTerm> candidateTerms);
    TextWithOccurrences highlight(String letter, CandidateTerm... candidateTerms);

}
