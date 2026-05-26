# GitLab GenAI Chatbot

A state-of-the-art, full-stack AI chatbot designed specifically to answer questions about GitLab using Retrieval-Augmented Generation (RAG). 

Instead of relying on an AI's generic knowledge, this application is highly specialized. It actively scrapes, processes, and embeds core sections of the official GitLab Handbook and documentation, storing that knowledge in a vectorized format. When a user asks a question, the application intelligently searches its database for the most relevant real-world GitLab documentation and uses a large language model to synthesize a perfectly accurate, context-aware answer.

## Key Features
- **Intelligent RAG Architecture:** Prevents AI hallucinations by strictly enforcing that the chatbot only answers using the ingested GitLab context.
- **Deep GitLab Knowledge:** The bot is currently trained on GitLab's All-Remote Culture, CI/CD pipelines, Engineering Workflow, Security Protocols, and Pricing strategies.
- **Premium User Interface:** A stunning, modern frontend utilizing dark mode aesthetics, dynamic glassmorphism, fluid micro-animations, and glowing interactive elements.
- **Automated Data Ingestion:** Includes a custom asynchronous scraper that automatically chunks and embeds large web pages into the vector database while respecting Google's API rate limits.

## Technology Stack
- **Frontend:** React, Vite, Vanilla CSS (Glassmorphism UI)
- **Backend:** Java, Spring Boot 3, Spring AI
- **Vector Database:** Supabase (PostgreSQL with `pgvector` extension)
- **AI Models:** Google Gemini (`gemini-2.5-flash` for chat, `gemini-embedding-2` for vector embeddings)
- **Deployment Ready:** Configured for seamless deployment on Vercel (Frontend) and Render (Backend).
