# GitLab GenAI Chatbot

This project is a Retrieval-Augmented Generation (RAG) chatbot that helps users learn about GitLab's handbook and direction pages.

## Architecture
- **Backend**: Java 17, Spring Boot 3.2.5, Spring AI
- **Database**: Supabase PostgreSQL with `pgvector`
- **Frontend**: React (Vite) + Vanilla CSS for premium UI
- **AI Model**: Google Gemini (via OpenAI compatibility API)

## How to Run Locally

### 1. Database & AI Setup
1. Create a `.env` file in the `backend/` directory with the following variables:
   ```env
   DATABASE_URL=jdbc:postgresql://<your-supabase-pooler-url>:5432/postgres
   DATABASE_USERNAME=postgres.<your-project-ref>
   DATABASE_PASSWORD=<your-database-password>
   GEMINI_API_KEY=<your-google-ai-studio-key>
   ```

### 2. Run the Backend
Navigate to the `backend/` directory:
```bash
./mvnw spring-boot:run
```
The server will start on `http://localhost:8080`.

**Ingest Data**: To populate the vector database with the GitLab handbook, send a POST request to:
```bash
curl -X POST http://localhost:8080/api/admin/ingest
```
Check the backend logs to see the scraping and embedding process!

### 3. Run the Frontend
Navigate to the `frontend/` directory:
```bash
npm install
npm run dev
```
Open `http://localhost:5173` in your browser to chat with the bot.

## Deployment
- The backend contains a `Dockerfile` and is ready to be deployed to Render.
- The frontend is a standard Vite React app and can be easily deployed to Vercel.
