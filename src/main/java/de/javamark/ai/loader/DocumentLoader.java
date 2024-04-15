package de.javamark.ai.loader;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import io.quarkus.logging.Log;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

public class DocumentLoader {
    public static Document loadSingleDocument() {
        URL res = DocumentLoader.class.getClassLoader().getResource("data/pdfs/APP.4.4 Kubernetes.pdf.clean.pdf");
        try {
            Path path = Paths.get(res.toURI());
            Log.infof("Loading single document: %s", path);
            return loadDocument(path, new ApacheTikaDocumentParser());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Document> loadMultipleDocumentsWithGlob() {
        URL res = DocumentLoader.class.getClassLoader().getResource("data/pdfs/");
        try {
            Path path = Paths.get(res.toURI());
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.pdf");
            List<Document> documents = loadDocuments(path, pathMatcher, new ApacheTikaDocumentParser());
            documents.forEach(DocumentLoader::log);
            return documents;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private static void log(Document document) {
        Log.infof("%s: %s ...", document.metadata("file_name"), document.text().trim().substring(0, 50));
    }
}
