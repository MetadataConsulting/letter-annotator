package org.modelcatalogue.letter.annotator;


import java.io.IOException;

/**
 * Base class for LetterAnnotator which helps to handle syntactic suger methods.
 */
public abstract class AbstractLetterAnnotator implements LetterAnnotator {

    @Override
    public final void addCandidate(CandidateTerm.Builder candidateTerm) throws IOException {
        addCandidate(candidateTerm.build());
    }

}
