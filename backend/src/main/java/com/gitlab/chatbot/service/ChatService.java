package com.gitlab.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful and polite GitLab assistant. " +
                        "Your job is to answer questions about GitLab's handbook and direction pages. " +
                        "You MUST only use the provided context to answer the question. " +
                        "If the answer is not in the context, politely say 'I'm sorry, but I couldn't find that information in the GitLab handbook.'")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder().topK(5).build()).build())
                .build();
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
