package org.modelcatalogue.letter.annotator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class represents candidate term which occurrences should be highlighted in the letter.
 *
 * This is an immutable value class. Use <code>Term#create(String)</code> to create a new builder and call
 * its <code>Builder#build()</code> method to create a term.
 */
public final class CandidateTerm {

    /**
     * Crates a builder for new candidate term.
     * @param term the term
     * @return the new builder with the term set
     */
    public static Builder create(String term) {
        if (term == null) {
            throw new IllegalArgumentException("Term cannot be null!");
        }
        if (term.length() == 0) {
            throw new IllegalArgumentException("Term cannot be empty!");
        }
        return new Builder(term);
    }

    public final static class Builder {
        private final String term;
        private URL url;
        private String title;
        private String description;
        private String pattern;
        private Map<String, Object> extensions = new LinkedHashMap<String, Object>();

        protected Builder(String term) {
            this.term = term;
        }

        /**
         * Adds a URL to the term definition.
         * @param url the URL for term definition
         */
        public Builder url(String url) throws MalformedURLException {
            this.url = new URL(url);
            return this;
        }

        /**
         * Adds a URL to the term definition.
         * @param url the URL for term definition
         */
        public Builder url(URL url) {
            this.url = url;
            return this;
        }

        /**
         * Adds a title to the term definition.
         * @param title the title for term definition
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Adds a description to the term definition.
         * @param description the description for term definition
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the regular expression for the builder.
         *
         * It mustn't contain any capturing groups!
         *
         * The flags are predefined in <code>Highlighter#HIGHLIGHTER_PATTERN_FLAGS</code> and cannot be changed.
         *
         * @param pattern the regular expression for the builder
         */
        public Builder pattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        /**
         * Adds the key, value pair of extension to the term definition. The extension meaning is derived from
         * the actual Highlighter or LetterAnnotator used.
         *
         * @param key the key of the extension
         * @param value the value of the extension
         */
        public Builder extensions(String key, Object value) {
            this.extensions.put(key, value);
            return this;
        }

        /**
         * Adds the key, value pairs of extensions to the term definition. The extension meaning is derived from
         * the actual Highlighter or LetterAnnotator used.
         *
         * @param extensions map of the extensions
         */
        public Builder extensions(Map<String, Object> extensions) {
            this.extensions.putAll(extensions);
            return this;
        }

        /**
         * @return a new candidate term based of current values preset in this builder
         */
        public CandidateTerm build() {
            return new CandidateTerm(term, url, title, description, pattern, Collections.unmodifiableMap(extensions));
        }
    }

    private final String term;
    private final URL url;
    private final String title;
    private final String description;
    private final Map<String, Object> extensions;
    private final Pattern pattern;

    protected CandidateTerm(String term, URL url, String title, String description, String pattern,  Map<String, Object> extensions) {
        this.term = term;
        this.url = url;
        this.title = title;
        this.description = description;
        this.extensions = extensions;
        if (pattern != null) {
            this.pattern = Pattern.compile(pattern, Highlighter.HIGHLIGHTER_PATTERN_FLAGS);
        } else {
            this.pattern = null;
        }
    }

    /**
     * @return the candidate term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @return the URL to the term definition if present
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return the title of the term
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description of the term
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the pattern for matching this term which has greater priority than matching by the term's term itself
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * @return map of all extensions present
     */
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CandidateTerm that = (CandidateTerm) o;

        if (!term.equals(that.term)) {
            return false;
        }
        if (url != null ? !url.equals(that.url) : that.url != null) {
            return false;
        }
        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (extensions != null ? !extensions.equals(that.extensions) : that.extensions != null) {
            return false;
        }
        return !(pattern != null ? !pattern.equals(that.pattern) : that.pattern != null);

    }

    @Override
    public int hashCode() {
        int result = term.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (extensions != null ? extensions.hashCode() : 0);
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateTerm{" +
                "term='" + term + '\'' +
                ", url=" + url +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", extensions=" + extensions +
                ", pattern=" + pattern +
                '}';
    }
}