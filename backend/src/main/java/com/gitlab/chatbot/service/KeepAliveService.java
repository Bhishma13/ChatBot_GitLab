package com.gitlab.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KeepAliveService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 840000)
    public void pingServer() {
        try {
            String url = "https://gitlab-backend.onrender.com/api/admin/health";
            restTemplate.getForObject(url, String.class);
            log.info("Keep-alive ping sent successfully");
        } catch (Exception e) {
            log.error("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}
