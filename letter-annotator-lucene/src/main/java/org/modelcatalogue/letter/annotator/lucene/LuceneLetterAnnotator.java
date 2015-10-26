package org.modelcatalogue.letter.annotator.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.modelcatalogue.letter.annotator.*;

import java.io.IOException;
import java.util.*;

public class LuceneLetterAnnotator extends AbstractLetterAnnotator {

    private static final String TERM_FIELD = "term";
    private static final String BODY_FIELD = "body";

    private final Directory termsDirectory;
    private final Analyzer analyzer;

    private final Map<String, CandidateTerm> candidateTermsMap = new LinkedHashMap<String, CandidateTerm>();

    private IndexWriter termsIndexWriter;



    public LuceneLetterAnnotator(){
        this(new RAMDirectory(), new EnglishAnalyzer());
    }

    public LuceneLetterAnnotator(Analyzer analyzer) {
        this(new RAMDirectory(), analyzer);
    }

    public LuceneLetterAnnotator(Directory termsDirectory) {
        this(termsDirectory, new EnglishAnalyzer());
    }

    public LuceneLetterAnnotator(Directory termsDirectory, Analyzer analyzer) {
        this.termsDirectory = termsDirectory;
        this.analyzer = analyzer;
    }

    @Override
    public void addCandidate(CandidateTerm candidateTerm) throws IOException {
        if (candidateTermsMap.containsKey(candidateTerm.getTerm())) {
            // first candidate term wins
            return;
        }

        candidateTermsMap.put(candidateTerm.getTerm(), candidateTerm);

        if (termsIndexWriter == null) {
            termsIndexWriter = createTermsIndexWriter();
        }

        Document doc = new Document();
        doc.add(new Field(TERM_FIELD, candidateTerm.getTerm(), createTermsFieldType()));

        termsIndexWriter.addDocument(doc);
    }


    @Override
    public AnnotatedLetter annotate(CharSequence letter, Highlighter highlighter) throws IOException {
        if (letter == null) {
            throw new IllegalArgumentException("Letter cannot be nulll");
        }

        if (letter.length() == 0) {
            throw new IllegalArgumentException("Letter cannot be empty");
        }

        closeAndRemoveIndexWriterIfNeeded();

        Directory letterDirectory = createLetterDirectory();

        indexLetter(letter, letterDirectory);

        LeafReader leafTermsReader = SlowCompositeReaderWrapper.wrap(DirectoryReader.open(termsDirectory));
        Terms termsTerms = leafTermsReader.terms(TERM_FIELD);
        TermsEnum termsTermsEnum = termsTerms.iterator();


        LeafReader leafBodyReader = SlowCompositeReaderWrapper.wrap(DirectoryReader.open(letterDirectory));
        Terms bodyTerms = leafBodyReader.terms(BODY_FIELD);
        TermsEnum bodyTermsEnum = bodyTerms.iterator();

        Set<Integer> candidateDocIds = new HashSet<Integer>();

        BytesRef bodyTermRef;
        while ((bodyTermRef = bodyTermsEnum.next()) != null) {
            if (termsTermsEnum.seekExact(bodyTermRef)) {
                PostingsEnum postings = termsTermsEnum.postings(null);
                Integer docid;
                while((docid = postings.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                    candidateDocIds.add(docid);
                }

            }
        }

        Set<CandidateTerm> candidateTerms = new HashSet<CandidateTerm>();

        for (Integer docid : candidateDocIds) {
            String termString = leafTermsReader.document(docid).get(TERM_FIELD);
            candidateTerms.add(candidateTermsMap.get(termString));
        }

        return new AnnotatedLetter(letter.toString(), candidateTerms, highlighter);
    }

    protected FieldType createLetterFieldType() {
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        type.setStoreTermVectorOffsets(true);
        type.setStoreTermVectorPositions(true);
        return type;
    }

    protected Directory createLetterDirectory() {
        return new RAMDirectory();
    }

    protected FieldType createTermsFieldType() {
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setStoreTermVectorPositions(true);
        type.setStoreTermVectorOffsets(true);
        return type;
    }

    protected IndexWriter createTermsIndexWriter() throws IOException {
        return new IndexWriter(termsDirectory, new IndexWriterConfig(analyzer));
    }

    protected IndexWriter createLetterIndexWriter(Directory letterDirectory) throws IOException {
        return new IndexWriter(letterDirectory, new IndexWriterConfig(analyzer));
    }

    private void indexLetter(CharSequence letter, Directory letterDirectory) throws IOException {
        IndexWriter bodyWriter = createLetterIndexWriter(letterDirectory);

        Document doc = new Document();
        doc.add(new Field(BODY_FIELD, letter.toString(), createLetterFieldType()));

        bodyWriter.addDocument(doc);
        bodyWriter.close();
    }

    private void closeAndRemoveIndexWriterIfNeeded() throws IOException {
        if (termsIndexWriter != null) {
            termsIndexWriter.close();
            termsIndexWriter = null;
        }
    }
}
