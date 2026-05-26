package com.gitlab.chatbot.controller;

import com.gitlab.chatbot.service.DataIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataIngestionService dataIngestionService;

    @PostMapping("/ingest")
    public ResponseEntity<String> triggerIngestion() {

        new Thread(dataIngestionService::ingestData).start();
        return ResponseEntity.ok("Data ingestion started in the background. Check logs for progress.");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
