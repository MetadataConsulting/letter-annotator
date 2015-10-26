package org.modelcatalogue.letter.annotator

import spock.lang.Specification

class HtmlHighlighterSpec extends Specification {

    def "test span replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holy grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").build()) == """
            Let's talk about the <span title="Holy Grail">holy grail</span>! It is so interesting!
        """
    }

    def "test ref replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holy grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").url("http://www.imdb.com/title/tt0071853/").build()) == """
            Let's talk about the <a href="http://www.imdb.com/title/tt0071853/" title="Holy Grail">holy grail</a>! It is so interesting!
        """
    }

    def "test regexp replacement"() {
        expect:
        Highlighter.HTML.highlight("""
            Let's talk about the holly grail! It is so interesting!
        """, CandidateTerm.create("Holy Grail").url("http://www.imdb.com/title/tt0071853/").pattern(/Holl?y Grail/).build()) == """
            Let's talk about the <a href="http://www.imdb.com/title/tt0071853/" title="Holy Grail">holly grail</a>! It is so interesting!
        """
    }

}
