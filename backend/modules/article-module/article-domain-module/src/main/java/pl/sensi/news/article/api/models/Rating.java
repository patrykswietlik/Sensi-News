package pl.sensi.news.article.api.models;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Rating(
        String id,
        String articleId,
        String userAccountId,
        LocalDateTime createdAt
) { }
