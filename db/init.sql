CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS vector_store (
    id SERIAL PRIMARY KEY,
    content TEXT,
    metadata JSONB,
    embedding vector(1536)
);

CREATE INDEX IF NOT EXISTS vector_store_embedding_idx
ON vector_store USING ivfflat (embedding vector_cosine_ops) 
WITH (lists = 100);
