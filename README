# F1 FIA Document RAG Application

A Spring Boot application that uses Retrieval-Augmented Generation (RAG) to answer questions about Formula 1 FIA documents. Upload your 60-page FIA regulation document and ask questions to get precise answers with source references.

### F1 FIA Doc

This application processes Formula 1 FIA documents (PDFs) by breaking them into searchable chunks, generating embeddings using OpenAI's API, and storing them in PostgreSQL with pgvector for similarity search. When users ask questions, the system retrieves the most relevant document sections and generates contextual answers using GPT.

### Features

- **PDF Document Processing**: Upload and automatically process FIA regulation documents
- **Intelligent Chunking**: Splits documents into optimally-sized chunks with overlapping context
- **Vector Search**: Uses PostgreSQL pgvector for fast similarity search across document embeddings
- **RAG-Powered Q&A**: Combines retrieved context with GPT to provide accurate, source-backed answers
- **Source Attribution**: Every answer includes references to the specific document sections used

### Technology Stack

- **Backend**: Spring Boot 3.2, Java 17
- **Database**: PostgreSQL with pgvector extension
- **AI/ML**: OpenAI GPT-3.5-turbo and text-embedding-ada-002
- **PDF Processing**: Apache PDFBox
- **Containerization**: Docker & Docker Compose

### Use Cases

- **Regulation Clarification**: Get clear explanations of complex FIA rules and regulations
- **Rule Lookup**: Quickly find specific information within lengthy regulation documents
- **Compliance Questions**: Ask about technical regulations, safety requirements, and sporting rules
- **Document Navigation**: Easily locate relevant sections without manually searching through 60+ pages