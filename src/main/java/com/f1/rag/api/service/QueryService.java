package com.f1.rag.api.service;

import com.f1.rag.api.repository.QueryRepository;
import com.f1.rag.dto.QueryRequestDto;
import com.f1.rag.models.Query;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    private final QueryRepository queryRepository;
    private final ModerationService moderationService;

    public QueryService(QueryRepository queryRepository, ModerationService moderationService) {
        this.queryRepository = queryRepository;
        this.moderationService = moderationService;
    }

    public String saveQueryFromUser(QueryRequestDto queryRequestDto) {
        Query newQuery = new Query(queryRequestDto.getQuery());
        queryRepository.save(newQuery);
        moderationService.moderateText(queryRequestDto.getQuery());

        return newQuery.getRawQuery();
    }

}
