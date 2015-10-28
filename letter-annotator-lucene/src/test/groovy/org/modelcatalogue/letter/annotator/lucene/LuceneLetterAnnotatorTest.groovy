package org.modelcatalogue.letter.annotator.lucene

import org.modelcatalogue.letter.annotator.AnnotatedLetter
import org.modelcatalogue.letter.annotator.CandidateTerm
import org.modelcatalogue.letter.annotator.LetterAnnotator
import spock.lang.Specification

class LuceneLetterAnnotatorTest extends Specification {


    def "Try annotate"() {
        when:
        String letterText = """
            This is some document about somatic problems. They can be caused by somatic mosaicism.
            Glad there were no neoplasm because initially thy were suspecting a neoplasm.
        """.stripIndent().trim()


        LetterAnnotator annotator = new LuceneLetterAnnotator()

        annotator.addCandidate(CandidateTerm.create('Neoplasm'))
        annotator.addCandidate(CandidateTerm.create('Somatic').url('https://en.wikipedia.org/wiki/Somatic_(biology)'))
        annotator.addCandidate(CandidateTerm.create('Mutation'))
        annotator.addCandidate(CandidateTerm.create('Somatic Mutation'))
        annotator.addCandidate(CandidateTerm.create('Somatic Mosaicism'))

        AnnotatedLetter letter = annotator.annotate(letterText)

        then:
        noExceptionThrown()
        letter

        letter.text == """
            This is some document about <a target="_blank" href="https://en.wikipedia.org/wiki/Somatic_(biology)" title="Somatic">somatic</a> problems. They can be caused by <span title="Somatic Mosaicism">somatic mosaicism</span>.
            Glad there were no <span title="Neoplasm">neoplasm</span> because initially thy were suspecting a <span title="Neoplasm">neoplasm</span>.
        """.stripIndent().trim()

        letter.occurrences.find { it.term.term == 'Neoplasm'}.positiveOccurrence == 2
    }

    def "run java example"() {
        when:
        JavaExample.main()
        then:
        noExceptionThrown()
    }
}
