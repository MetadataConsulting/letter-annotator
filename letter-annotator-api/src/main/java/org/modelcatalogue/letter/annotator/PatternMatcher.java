package org.modelcatalogue.letter.annotator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The simplest implementation of matcher possible using the regular expressions.
 *
 * The matcher either creates the patterns from the terms and their synonyms or uses the pattern
 * supplied by the term as the pattern extension.
 */
public class PatternMatcher extends AbstractTermMatcher {

    public static final int PATTERN_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;

    protected final AnnotatedLetter annotate(Highlighter highlighter, Scorer scorer, String letter, Map<String, CandidateTerm> candidateTerms) {
        Map<String, CandidateTerm> normalizedMap = new HashMap<String, CandidateTerm>();

        for (Map.Entry<String, CandidateTerm> termEntry : candidateTerms.entrySet()) {
            normalizedMap.put(normalize(termEntry.getKey()), termEntry.getValue());
        }

        final StringBuilder patternBuilder = new StringBuilder();
        for (Map.Entry<String, CandidateTerm> term : normalizedMap.entrySet()) {
            if (term.getValue().getPattern() == null) {
                patternBuilder.append("(?<=[\\W$])");
                patternBuilder.append(Pattern.quote(term.getKey()).replaceAll("\\s+", " "));
                patternBuilder.append("\\w*");
            } else {
                patternBuilder.append(term.getValue().getPattern().pattern());
            }
            patternBuilder.append("|");
        }
        final String patternString = patternBuilder.toString();
        final Pattern pattern = Pattern.compile(patternString.substring(0, patternString.lastIndexOf("|")), PATTERN_FLAGS);
        final Matcher matcher = pattern.matcher(letter);
        if (matcher.find()) {
            TermOccurrence.Collector collector = TermOccurrence.collect();
            final StringBuffer sb = new StringBuffer(letter.length() + 16);
            sb.append(highlighter.getHead());
            do {
                String originalMatch = matcher.group();
                String normalizedMatch = normalize(originalMatch);
                CandidateTerm matchedTerm = normalizedMap.get(normalizedMatch);
                if (matchedTerm == null) {
                    for (Map.Entry<String, CandidateTerm> entry : normalizedMap.entrySet()) {
                        if (entry.getValue().getPattern() != null) {
                            if (entry.getValue().getPattern().matcher(originalMatch).matches()) {
                                matchedTerm = entry.getValue();
                                break;
                            }
                        } else {
                            if (normalizedMatch.startsWith(entry.getKey())) {
                                matchedTerm = entry.getValue();
                                break;
                            }
                        }
                    }
                }
                if (matchedTerm == null) {
                    throw new IllegalStateException("No term found for '" + originalMatch + "'. Normalized terms map: " + normalizedMap);
                }
                scorer.score(collector, letter, originalMatch, matcher.start(), matcher.end(), matchedTerm);
                matcher.appendReplacement(sb, Matcher.quoteReplacement(highlighter.getReplacement(originalMatch, matchedTerm)));
            } while (matcher.find());
            matcher.appendTail(sb);
            sb.append(highlighter.getTail());
            return new AnnotatedLetter(sb.toString(), collector.getOccurrences());
        }
        return nothingToHighlight(highlighter, letter);
    }

    /**
     * Normalizes the string to be proper key in the terms map.
     * @param string the string to be normalized
     * @return the normalized string
     */
    protected String normalize(String string) {
        return string.toLowerCase().replaceAll("\\s+", " ");
    }
}
