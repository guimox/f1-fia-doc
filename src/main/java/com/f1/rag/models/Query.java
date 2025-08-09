package com.f1.rag.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Query {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;

    private String rawQuery;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Query() {
    }

    public Query(String rawQuery) {
        this.rawQuery = rawQuery;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
