package org.modelcatalogue.letter.annotator

import spock.lang.Specification

class HtmlHighlighterSpec extends Specification {

    def "test span replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holy grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").build()).text == """
            Let's talk about the <span title="Holy Grail">holy grail</span>! It is so interesting!
        """
    }

    def "test match to the end of the word replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holy grails! It is so interesting!
        """, CandidateTerm.create("Holy Grail").build()).text == """
            Let's talk about the <span title="Holy Grail">holy grails</span>! It is so interesting!
        """
    }

    def "test match only from the beginning of the word"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the melancholy grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").build()).text == """
            Let's talk about the melancholy grail! It is so interesting!
        """
    }

    def "test ref replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holy grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").url("http://www.imdb.com/title/tt0071853/").build()).text == """
            Let's talk about the <a target="_blank" href="http://www.imdb.com/title/tt0071853/" title="Holy Grail">holy grail</a>! It is so interesting!
        """
    }

    def "test regexp replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holly grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").url("http://www.imdb.com/title/tt0071853/").pattern(/Holl?y Grail/).build()).text == """
            Let's talk about the <a target="_blank" href="http://www.imdb.com/title/tt0071853/" title="Holy Grail">holly grail</a>! It is so interesting!
        """
    }

    def "test synonym replacement"() {
        String original = """
            Let's talk about the holy grail! It is so interesting! It's sometimes referred as Sang Real.
        """

        String expected = """
            Let's talk about the <a target="_blank" href="http://www.imdb.com/title/tt0071853/" title="Holy Grail">holy grail</a>! It is so interesting! It's sometimes referred as <a target="_blank" href="http://www.imdb.com/title/tt0071853/" title="Holy Grail">Sang Real</a>.
        """

        String highlighted = Highlighter.HTML.highlight(original, CandidateTerm.create("Holy Grail").synonym("Sang Real").url("http://www.imdb.com/title/tt0071853/").build()).text

        expect:
        highlighted == expected
    }

}
