# GitLab GenAI Chatbot: Project Write-up

## Objective
The goal of this project was to develop an interactive Generative AI chatbot capable of retrieving and structuring relevant data from GitLab's Handbook and Direction pages, allowing employees and users to easily access policies and roadmap information.

## Approach & Architecture

To ensure a robust, scalable, and modern application, I opted for a decoupled architecture:
1. **Backend API**: Java Spring Boot 3
2. **AI Integration**: Spring AI & Google Gemini
3. **Database**: Supabase (PostgreSQL with `pgvector`)
4. **Frontend**: React (Vite)

### 1. Data Processing & Ingestion (RAG Pipeline)
I implemented a `DataIngestionService` using **Jsoup** to programmatically scrape the target GitLab URLs. The raw HTML was parsed to extract the core `<main>` text, stripping out unnecessary navigation elements and footers to ensure the AI only receives high-quality context.

Using **Spring AI's TokenTextSplitter**, the text is chunked into optimal sizes to avoid exceeding token limits. These chunks are then passed to an embedding model (configured to use Google's `text-embedding-004` via the OpenAI compatibility API) and stored securely in Supabase using the `pgvector` extension.

### 2. Generative AI Chatbot
The core conversational logic relies on the **Retrieval-Augmented Generation (RAG)** pattern. When a user submits a query to the `/api/chat` endpoint, the `ChatService`:
1. Converts the user's query into an embedding.
2. Performs a similarity search in Supabase (Cosine Distance) to retrieve the top 5 most relevant handbook chunks.
3. Injects these chunks into a strict System Prompt. The prompt explicitly guardrails the model: *"You MUST only use the provided context... If the answer is not in the context, politely say 'I couldn't find that information.'"*
4. Calls the `gemini-1.5-flash` model to generate the final response.

### 3. Frontend / UI Development
Instead of a simple Streamlit UI, I built a custom **React application** to provide a "wow" factor and a premium user experience. I utilized **Vanilla CSS** with modern design trends like glassmorphism, smooth micro-animations, and dynamic gradient backgrounds. 
- The UI handles loading states (animated typing indicator) to manage user expectations.
- It parses Markdown seamlessly using `react-markdown`.
- Error handling gracefully displays a fallback message if the backend is unreachable.

## Decisions & Challenges

**Choosing Gemini via OpenAI Compatibility:**
Spring AI provides various native starters, but Vertex AI requires complex Google Cloud service accounts. To keep the project lightweight and free, I leveraged the recent update that allows the Gemini API to natively accept OpenAI-formatted requests. By using the `spring-ai-openai` starter and changing the base URL to Google's API, I achieved the power of Gemini using a simple API key.

**Database Selection:**
I chose **Supabase** because it natively supports `pgvector` out of the box, making it the perfect cloud database for storing AI embeddings without the overhead of deploying a custom vector database like Chroma or Milvus.

## Innovation & Product Thinking
To enhance the UX beyond basic requirements:
- **Guardrailing**: The system prompt is engineered to prevent hallucinations. The bot will refuse to answer questions outside the scope of the handbook.
- **Background Ingestion**: The data scraping endpoint runs asynchronously. This ensures that when an admin triggers an update, the HTTP request doesn't timeout while processing large amounts of handbook text.
- **Premium Aesthetics**: The frontend uses CSS variables, backdrop filters, and refined typography to mirror the premium feel of leading AI products.
