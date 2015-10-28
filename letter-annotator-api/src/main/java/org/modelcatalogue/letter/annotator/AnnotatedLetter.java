package org.modelcatalogue.letter.annotator;

import java.util.Collections;
import java.util.Set;

/**
 * Annotated letter holds the annotated text and the term occurences
 */
public final class AnnotatedLetter {

    private final String text;
    private final Set<TermOccurrence> occurrences;

    /**
     * Creates new annotated letter with given annotated text and the term occurences
     * @param text annotated letter
     * @param occurrences term occurences
     */
    public AnnotatedLetter(String text, Set<TermOccurrence> occurrences) {
        if (text == null) {
            throw new IllegalArgumentException("Letter cannot be null!");
        }
        if (occurrences == null) {
            throw new IllegalArgumentException("Statistics cannot be null!");
        }
        this.text = text;
        this.occurrences = Collections.unmodifiableSet(occurrences);
    }

    /**
     * @return the annotated text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the term occurences
     */
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

        AnnotatedLetter that = (AnnotatedLetter) o;

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
        return "AnnotatedLetter{" +
                "text='" + text + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}
