package pl.sensi.news.article.api.models;

import jakarta.validation.constraints.NotBlank;
import pl.sensi.news.article.ValidationMessageConst;

public record CommentSaveForm(
        @NotBlank(message = ValidationMessageConst.NO_CONTENT) String content,
        @NotBlank(message = ValidationMessageConst.NO_ARTICLE_ID) String articleId
) { }
