package org.modelcatalogue.letter.annotator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The simplest possible implementation of the annotator which simply creates new annotated letter with
 * all the candidate terms previously supplied.
 */
public class SimpleLetterAnnotator extends AbstractLetterAnnotator {

    private final Set<CandidateTerm> terms = new HashSet<CandidateTerm>();

    @Override
    public void addCandidate(CandidateTerm candidateTerm) throws IOException {
        terms.add(candidateTerm);
    }

    @Override
    public AnnotatedLetter annotate(CharSequence letter, Highlighter highlighter) throws IOException {
        if (letter == null) {
            throw new IllegalArgumentException("Letter cannot be null");
        }
        if (letter.length() == 0) {
            throw new IllegalArgumentException("Letter cannot be empty");
        }
        return new AnnotatedLetter(letter.toString(), new HashSet<CandidateTerm>(terms), highlighter);
    }
}
