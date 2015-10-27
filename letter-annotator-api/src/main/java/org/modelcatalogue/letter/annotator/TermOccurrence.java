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

            builder.increment();

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
        private int occurrence;

        protected Builder(CandidateTerm term) {
            this.term = term;
        }

        Builder occurrence(int occurrence) {
            if (occurrence < 0) {
                throw new IllegalArgumentException("Occurrence cannot negative!");
            }
            this.occurrence = occurrence;
            return this;
        }

        Builder increment() {
            this.occurrence++;
            return this;
        }

        TermOccurrence build() {
            return new TermOccurrence(term, occurrence);
        }
    }

    final CandidateTerm term;
    final int occurrence;

    protected TermOccurrence(CandidateTerm term, int occurrence) {
        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null");
        }
        if (occurrence < 0) {
            throw new IllegalArgumentException("Occurrence cannot negative!");
        }
        this.term = term;
        this.occurrence = occurrence;
    }

    public CandidateTerm getTerm() {
        return term;
    }

    public int getOccurrence() {
        return occurrence;
    }

    @Override
    public int compareTo(TermOccurrence o) {
        if (o == null) {
            return 1;
        }
        int occurrenceComparison = - ((Integer) occurrence).compareTo(o.occurrence);
        if (occurrenceComparison != 0) {
            return occurrenceComparison;
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

        if (occurrence != that.occurrence) {
            return false;
        }
        return term.equals(that.term);

    }

    @Override
    public int hashCode() {
        int result = term.hashCode();
        result = 31 * result + occurrence;
        return result;
    }

    @Override
    public String toString() {
        return "TermOccurrence{" +
                "term=" + term +
                ", occurrence=" + occurrence +
                '}';
    }
}
