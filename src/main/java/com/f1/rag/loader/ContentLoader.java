package com.f1.rag.loader;

import org.jsoup.Jsoup;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ContentLoader {

    private final VectorStore vectorStore;

    @Value("classpath*:pdfs/*.pdf")
    private Resource[] pdfResources;

    public ContentLoader(VectorStore vectorStore, Resource[] pdfResources) {
        this.vectorStore = vectorStore;
        this.pdfResources = pdfResources;
    }

    @PostConstruct
    public void init() {
        var existing = vectorStore.similaritySearch(SearchRequest.builder().query("test").topK(1).build());
        if (!existing.isEmpty()) {
            System.out.println("VectorStore already populated. Skipping content loading.");
            return;
        }

        var config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
                .build();

        List<Document> allDocuments = new ArrayList<>();

        for (Resource resource : pdfResources) {
            var pdfReader = new PagePdfDocumentReader(resource, config);
            allDocuments.addAll(pdfReader.get());
        }

        List<String> urls = List.of(
                "https://en.wikipedia.org/wiki/Formula_One_car",
                "https://en.wikipedia.org/wiki/Skid_block",
                "https://en.wikipedia.org/wiki/Formula_One_regulations"
        );

        for (String url : urls) {
            try {
                String content = Jsoup.connect
                        (url).get().body().text();
                allDocuments.add(new Document(content, Map.of("source", url)));
            } catch (IOException e) {
                System.err.println("Error fetching " + url + ": " + e.getMessage());
            }
        }

        var textSplitter = new TokenTextSplitter(512, 200, 100, 5000, true);
        var textSplitterWeb = new TokenTextSplitter(
                256,
                100,
                50,
                5000,
                true
        );

        vectorStore.accept(textSplitter.apply(allDocuments));
        vectorStore.accept(textSplitterWeb.apply(allDocuments));
    }
}
