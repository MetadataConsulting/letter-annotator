package org.modelcatalogue.letter.annotator.lucene;

import org.modelcatalogue.letter.annotator.AnnotatedLetter;
import org.modelcatalogue.letter.annotator.CandidateTerm;
import org.modelcatalogue.letter.annotator.LetterAnnotator;

import java.io.IOException;

public class JavaExample {
    
    public static void main(String... args) throws IOException {
        String letterText = "This is some document about somatic problems. They can be caused by somatic mosaicism. " +
                "Glad there were no neoplasm because initially thy were suspecting a neoplasm";

        String expected = "This is some document about <a target=\"_blank\" href=\"https://en.wikipedia.org/wiki/Somatic\" " +
                "title=\"Somatic\">somatic</a> problems. They can be caused by <span title=\"Somatic Mosaicism\" " +
                "class=\"visited\">somatic mosaicism</span>. Glad there were no <span title=\"Neoplasm\">" +
                "neoplasm</span> because initially thy were suspecting a <span title=\"Neoplasm\">neoplasm</span>";

        LetterAnnotator annotator = new LuceneLetterAnnotator();

        annotator.addCandidate(CandidateTerm.create("Neoplasm"));
        annotator.addCandidate(CandidateTerm.create("Somatic").url("https://en.wikipedia.org/wiki/Somatic"));
        annotator.addCandidate(CandidateTerm.create("Mutation"));
        annotator.addCandidate(CandidateTerm.create("Somatic Mutation"));
        annotator.addCandidate(CandidateTerm.create("Somatic Mosaicism").extensions("class", "visited"));

        AnnotatedLetter letter = annotator.annotate(letterText);

        if (!letter.getText().equals(expected)) {
            throw new RuntimeException("Text does not match!");
        }
        System.out.println("Expected output matched!");
        System.out.println("========================");
        System.out.println(letter.getText());
        System.out.println("========================");
    }
    
}
