package pl.sensi.news.article.api.models;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ArticleUpdateForm(
        String id,
        String title,
        String content,
        List<String> tags,
        String image
) { }
