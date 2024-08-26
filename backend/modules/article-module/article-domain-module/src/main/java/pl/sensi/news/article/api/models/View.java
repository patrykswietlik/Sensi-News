package pl.sensi.news.article.api.models;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record View(
        String id,
        String articleId,
        String userIpAddress,
        LocalDateTime createdAt
) { }
