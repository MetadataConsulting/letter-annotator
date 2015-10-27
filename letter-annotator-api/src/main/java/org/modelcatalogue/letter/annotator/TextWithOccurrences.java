package org.modelcatalogue.letter.annotator;

import java.util.Collections;
import java.util.Set;

public class TextWithOccurrences {

    private final String text;
    private final Set<TermOccurrence> occurrences;

    public TextWithOccurrences(String text, Set<TermOccurrence> occurrences) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null!");
        }
        if (occurrences == null) {
            throw new IllegalArgumentException("Statistics cannot be null!");
        }
        this.text = text;
        this.occurrences = Collections.unmodifiableSet(occurrences);
    }

    public String getText() {
        return text;
    }

    public Set<TermOccurrence> getOccurrences() {
        return occurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextWithOccurrences that = (TextWithOccurrences) o;

        if (!text.equals(that.text)) {
            return false;
        }
        return occurrences.equals(that.occurrences);

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + occurrences.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TextWithOccurrences{" +
                "text='" + text + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}
