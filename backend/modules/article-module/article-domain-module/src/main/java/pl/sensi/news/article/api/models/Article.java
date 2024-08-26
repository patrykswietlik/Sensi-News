package pl.sensi.news.article.api.models;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record Article(
        String id,
        String title,
        String userAccountId,
        String userAccountUsername,
        String content,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> tagsIds,
        int views,
        int likesCount,
        String status
) { }
