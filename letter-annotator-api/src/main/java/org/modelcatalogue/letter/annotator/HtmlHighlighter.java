package org.modelcatalogue.letter.annotator;

import java.util.Map;

/**
 * Highlights the term match with either <code>span</code> or <code>a</code> (if url is present in a term) elements.
 * The <code>description</code> is using <code>data-content</code> attribute to be compatible with Bootstrap's Popover.
 *
 * @see Highlighter#HTML
 */
public class HtmlHighlighter extends AbstractHighlighter {

    @Override
    public String getReplacement(String matchedString, CandidateTerm term) {
        StringBuilder builder = new StringBuilder();

        if (term.getUrl() != null) {
            builder.append("<a target=\"_blank\"");
            appendAttribute(builder, "href", term.getUrl().toExternalForm());
        } else {
            builder.append("<span");
        }

        appendAttribute(builder, "title", term.getTitle(), term.getTerm());
        appendAttribute(builder, "data-content", term.getDescription());

        for (Map.Entry<String, Object> extension : term.getExtensions().entrySet()) {
            appendAttribute(builder, extension.getKey(), extension.getValue());
        }

        builder.append(">");

        builder.append(matchedString);

        if (term.getUrl() != null) {
            builder.append("</a>");
        } else {
            builder.append("</span>");
        }

        return builder.toString();
    }

    private static void appendAttribute(StringBuilder builder, String name, Object... pickFirst) {
        if (pickFirst == null || pickFirst.length == 0) {
            return;
        }

        Object value = null;

        for (Object val : pickFirst) {
            if (val != null) {
                value = val;
                break;
            }
        }

        if (value == null) {
            return;
        }

        builder.append(" ");
        builder.append(escapeHTML(name));
        builder.append("=\"");
        builder.append(escapeHTML(value));
        builder.append("\"");
    }


    // http://stackoverflow.com/questions/1265282/recommended-method-for-escaping-html-in-java
    private static String escapeHTML(Object value) {
        if (value == null) {
            return null;
        }

        String s = value.toString();

        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
                out.append("&#");
                out.append((int) c);
                out.append(';');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
