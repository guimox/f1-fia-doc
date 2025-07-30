package com.f1.rag;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/upload")
public class PdfController {

    @Autowired
    private PdfEmbeddingRepository repository;

    @Autowired
    private OpenAiEmbeddingService embeddingService;

    @GetMapping
    public String uploadPdf() {
        try {
            InputStream inputStream = new ClassPathResource("sample.pdf").getInputStream();
            PDDocument document = PDDocument.load(inputStream);
            String text = new PDFTextStripper().getText(document);
            document.close();

            float[] vector = embeddingService.getEmbedding(text);

            PdfEmbedding entry = new PdfEmbedding();
            entry.setContent(text);
            entry.setEmbedding(new com.pgvector.PGvector(vector));

            repository.save(entry);

            return "Uploaded with ID: " + entry.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
