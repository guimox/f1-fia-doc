package com.f1.rag.api.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/web")
public class WebContentController {

    @GetMapping("/extract")
    public ResponseEntity<String> extractWebContent(@RequestParam String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();
            return ResponseEntity.ok(text);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error fetching content: " + e.getMessage());
        }
    }
}

