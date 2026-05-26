package com.gitlab.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataIngestionService {

    private final VectorStore vectorStore;

    private static final List<String> TARGET_URLS = List.of(
            "https://handbook.gitlab.com/handbook/company/culture/all-remote/",
            "https://docs.gitlab.com/ee/ci/",
            "https://about.gitlab.com/pricing/",
            "https://handbook.gitlab.com/handbook/engineering/workflow/",
            "https://handbook.gitlab.com/handbook/security/"
    );

    public void ingestData() {
        log.info("Starting data ingestion from GitLab Handbook...");
        
        for (String url : TARGET_URLS) {
            try {
                log.info("Scraping URL: {}", url);
                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                

                Element contentElement = doc.selectFirst("main");
                if (contentElement == null) {
                    contentElement = doc.body();
                }

                String text = contentElement.text();
                

                Document document = new Document(text, Map.of("source", url, "title", doc.title()));


                TokenTextSplitter splitter = new TokenTextSplitter();
                List<Document> chunks = splitter.apply(List.of(document));
                
                log.info("Split {} into {} chunks. Saving to vector store...", url, chunks.size());
                

                vectorStore.add(chunks);
                
                log.info("Successfully ingested {}", url);

            } catch (IOException e) {
                log.error("Failed to scrape URL: {}", url, e);
            } catch (RuntimeException e) {
                log.warn("Encountered API error (likely 429 Rate Limit) for URL: {}. Pausing for 60 seconds before continuing...", url);
                try {
                    Thread.sleep(60000);
                    log.info("Retrying URL: {}", url);
                    org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                    Element contentElement = doc.selectFirst("main");
                    if (contentElement == null) contentElement = doc.body();
                    String text = contentElement.text();
                    Document document = new Document(text, Map.of("source", url, "title", doc.title()));
                    List<Document> chunks = new TokenTextSplitter().apply(List.of(document));
                    vectorStore.add(chunks);
                    log.info("Successfully ingested {} on retry", url);
                } catch (Exception retryEx) {
                    log.error("Retry failed for URL: {}. Skipping.", url, retryEx);
                }
            }
        }
        
        log.info("Data ingestion complete.");
    }
}
