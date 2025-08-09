package com.f1.rag.api.repository;

import com.f1.rag.models.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QueryRepository extends JpaRepository<Query, UUID> {
}
