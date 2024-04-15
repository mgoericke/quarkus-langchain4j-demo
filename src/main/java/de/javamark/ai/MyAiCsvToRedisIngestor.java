package de.javamark.ai;

import de.javamark.ai.loader.DocumentLoader;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

@ApplicationScoped
public class MyAiCsvToRedisIngestor {
    @ConfigProperty(name = "csv.file")
    File file;

    @ConfigProperty(name = "csv.headers")
    List<String> headers;

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    RedisEmbeddingStore embeddingStore;

    public void ingest(@Observes StartupEvent event) throws IOException {
        // Configure the CSV format.
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers.toArray(new String[0]))
                .setSkipHeaderRecord(true)
                .build();
        int count = 0;
        List<Document> documents = new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                Map<String, String> metadata = new HashMap<>();
                metadata.put("source", file.getAbsolutePath());
                metadata.put("row", String.valueOf(count++));

                StringBuilder content = new StringBuilder();
                for (String header : headers) {
                    // Include all headers in the metadata.
                    metadata.put(header, record.get(header));
                    content.append(header).append(": ").append(record.get(header)).append("\n");
                }

                Document document = new Document(content.toString(), Metadata.from(metadata));
                documents.add(document);
                count++;
            }
            Log.infof("Ingested %d movies.%n", count);
        }
        var ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore) // Injected
                .embeddingModel(embeddingModel) // Injected
                .documentSplitter(recursive(500, 0))
                .build();
        ingestor.ingest(documents);

        //ingestor.ingest(DocumentLoader.loadMultipleDocumentsWithGlob());

        ingestor.ingest(DocumentLoader.loadSingleDocument());
    }
}
