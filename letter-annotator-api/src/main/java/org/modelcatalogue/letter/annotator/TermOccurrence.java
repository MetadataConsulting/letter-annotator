package org.modelcatalogue.letter.annotator;

import java.util.*;

public final class TermOccurrence implements Comparable<TermOccurrence> {

    public static Collector collect() {
        return new Collector();
    }

    public static final class Collector {
        private final Map<CandidateTerm, Builder> occurrences = new HashMap<CandidateTerm, Builder>();

        public Collector increment(CandidateTerm term) {
            Builder builder = occurrences.get(term);

            if (builder == null) {
                builder = create(term);
                occurrences.put(term, builder);
            }

            builder.positive();

            return this;
        }

        public Set<TermOccurrence> getOccurrences() {
            TreeSet<TermOccurrence> result = new TreeSet<TermOccurrence>();
            for (Builder builder : occurrences.values()) {
                result.add(builder.build());
            }
            return Collections.unmodifiableSet(result);
        }

    }

    protected static Builder create(CandidateTerm term) {
        return new Builder(term);
    }

    protected static final class Builder {
        private final CandidateTerm term;
        private int positiveOccurrence;
        private int negativeOccurrence;

        protected Builder(CandidateTerm term) {
            this.term = term;
        }

        Builder positive() {
            this.positiveOccurrence++;
            return this;
        }

        Builder negative() {
            this.negativeOccurrence++;
            return this;
        }

        TermOccurrence build() {
            return new TermOccurrence(term, positiveOccurrence, negativeOccurrence);
        }
    }

    final CandidateTerm term;
    final int positiveOccurrence;
    final int negativeOccurrence;

    protected TermOccurrence(CandidateTerm term, int positiveOccurrence, int negativeOccurrence) {
        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null");
        }
        if (positiveOccurrence < 0) {
            throw new IllegalArgumentException("Positive occurrence cannot negative!");
        }
        if (negativeOccurrence < 0) {
            throw new IllegalArgumentException("negative occurrence cannot negative!");
        }
        this.term = term;
        this.positiveOccurrence = positiveOccurrence;
        this.negativeOccurrence = negativeOccurrence;
    }

    public CandidateTerm getTerm() {
        return term;
    }

    public int getPositiveOccurrence() {
        return positiveOccurrence;
    }

    public int getNegativeOccurrence() {
        return negativeOccurrence;
    }

    @Override
    public int compareTo(TermOccurrence o) {
        if (o == null) {
            return 1;
        }
        int positiveOccurrenceComparison = - ((Integer) positiveOccurrence).compareTo(o.positiveOccurrence);
        if (positiveOccurrenceComparison != 0) {
            return positiveOccurrenceComparison;
        }
        int negativeOccurrenceComparison = ((Integer) negativeOccurrence).compareTo(o.negativeOccurrence);
        if (negativeOccurrenceComparison != 0) {
            return negativeOccurrenceComparison;
        }
        return term.getTerm().compareTo(o.term.getTerm());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TermOccurrence that = (TermOccurrence) o;

        if (positiveOccurrence != that.positiveOccurrence) {
            return false;
        }
        if (negativeOccurrence != that.negativeOccurrence) {
            return false;
        }
        return term.equals(that.term);

    }

    @Override
    public int hashCode() {
        int result = term.hashCode();
        result = 31 * result + positiveOccurrence;
        result = 31 * result + negativeOccurrence;
        return result;
    }

    @Override
    public String toString() {
        return "TermOccurrence{" +
                "term=" + term +
                ", positiveOccurrence=" + positiveOccurrence +
                ", negativeOccurrence=" + negativeOccurrence +
                '}';
    }
}
