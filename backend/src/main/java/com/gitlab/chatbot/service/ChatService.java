package com.gitlab.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful and polite GitLab assistant. " +
                        "Your job is to answer questions about GitLab's handbook and direction pages. " +
                        "You MUST prioritize the provided context to answer specific questions. " +
                        "However, for basic introductory questions like 'What is GitLab', and casual conversational phrases (like 'hi', 'ok', 'bye', 'thanks'), you may use your general knowledge to respond naturally. " +
                        "Keep your initial answers very brief and concise (maximum 2 to 3 sentences). Do not provide long explanations unless the user explicitly asks you to 'explain more' or 'give details'. " +
                        "If the user asks about an unrelated topic or something not in the context, politely reply with: 'I don't know about that, please ask me questions related to GitLab only.'")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder().topK(5).build()).build())
                .build();
    }

    public String chat(String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param("chat_memory_conversation_id", chatId))
                .call()
                .content();
    }
}
