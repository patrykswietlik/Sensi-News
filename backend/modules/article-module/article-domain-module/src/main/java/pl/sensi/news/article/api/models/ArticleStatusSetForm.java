package pl.sensi.news.article.api.models;

import jakarta.validation.constraints.NotEmpty;

public record ArticleStatusSetForm(
        @NotEmpty String status
) { }
