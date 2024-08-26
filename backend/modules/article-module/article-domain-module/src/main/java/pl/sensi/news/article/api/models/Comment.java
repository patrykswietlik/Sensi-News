package pl.sensi.news.article.api.models;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record Comment(
        String id,
        String content,
        String articleId,
        String userAccountId,
        String userAccountUsername,
        String replyToId,
        List<Comment> replies,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int version
) { }
