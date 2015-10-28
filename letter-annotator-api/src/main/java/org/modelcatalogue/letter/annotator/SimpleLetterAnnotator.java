package org.modelcatalogue.letter.annotator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The simplest possible implementation of the annotator which simply creates new annotated letter with
 * all the candidate terms previously supplied.
 */
public class SimpleLetterAnnotator extends AbstractLetterAnnotator {

    private final Set<CandidateTerm> terms = new HashSet<CandidateTerm>();

    @Override
    public void addCandidate(CandidateTerm candidateTerm) {
        terms.add(candidateTerm);
    }

    @Override
    protected Collection<CandidateTerm> getCandidateTerms(CharSequence letter) {
        return terms;
    }
}
