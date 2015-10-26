package org.modelcatalogue.letter.annotator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The base implementation for highlighters which replaces the string matched by either the term of the term candidate
 * or by its pattern by the string with the more information from the candidate term encoded.
 *
 * @see #getReplacement(String, CandidateTerm)
 */
public abstract class ReplacementHighlighter extends AbstractHighlighter {

    @Override
    protected final String highlight(String letter, Map<String, CandidateTerm> candidateTerms) {
        Map<String, CandidateTerm> normalizedMap = new HashMap<String, CandidateTerm>();

        for (Map.Entry<String, CandidateTerm> termEntry : candidateTerms.entrySet()) {
            normalizedMap.put(normalize(termEntry.getKey()), termEntry.getValue());
        }

        final StringBuilder patternBuilder = new StringBuilder();
        for (Map.Entry<String, CandidateTerm> term : normalizedMap.entrySet()) {
            if (term.getValue().getPattern() == null) {
                patternBuilder.append(Pattern.quote(term.getKey()).replaceAll("\\s+", " "));
            } else {
                patternBuilder.append(term.getValue().getPattern().pattern());
            }
            patternBuilder.append("|");
        }
        final String patternString = patternBuilder.toString();
        final Pattern pattern = Pattern.compile(patternString.substring(0, patternString.lastIndexOf("|")), HIGHLIGHTER_PATTERN_FLAGS);
        final Matcher matcher = pattern.matcher(letter);
        if (matcher.find()) {
            final StringBuffer sb = new StringBuffer(letter.length() + 16);
            do {
                String originalMatch = matcher.group();
                CandidateTerm matchedTerm = normalizedMap.get(normalize(originalMatch));
                if (matchedTerm == null) {
                    for (CandidateTerm term : candidateTerms.values()) {
                        if (term.getPattern() != null) {
                            if (term.getPattern().matcher(originalMatch).matches()) {
                                matchedTerm = term;
                                break;
                            }
                        }
                    }
                }
                if (matchedTerm == null) {
                    throw new IllegalStateException("No term found for '" + originalMatch + "'. Normalized terms map: " + normalizedMap);
                }
                matcher.appendReplacement(sb, Matcher.quoteReplacement(getReplacement(originalMatch, matchedTerm)));
            } while (matcher.find());
            matcher.appendTail(sb);
            appendTail(sb);
            return sb.toString();
        } else {
            StringBuffer buffer = new StringBuffer(letter);
            appendTail(buffer);
            return buffer.toString();
        }
    }

    /**
     * Normalizes the string to be proper key in the terms map.
     * @param string the string to be normalized
     * @return the normalized string
     */
    protected String normalize(String string) {
        return string.toLowerCase().replaceAll("\\s+", " ");
    }

    /**
     * Appends the text to the end of the buffer.
     * @param buffer the buffer to be appended
     */
    protected void appendTail(StringBuffer buffer) {
        // do nothing by default
    }

    /**
     * Returns the replacement of matched string usually with the information from the term encoded.
     * @param matchedString the matched string
     * @param term ther term matched to given matched string
     * @return the replacement of matched string usually with the information from the term encoded
     */
    protected abstract String getReplacement(String matchedString, CandidateTerm term);
}
