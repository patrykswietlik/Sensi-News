package pl.sensi.news.article.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import pl.sensi.news.article.ValidationMessageConst;

import java.util.List;

public record ArticleRequestForm(
        @NotBlank(message = ValidationMessageConst.NO_TITLE) String title,
        @NotBlank(message = ValidationMessageConst.NO_CONTENT) String content,
        byte[] image,
        @NotEmpty(message = ValidationMessageConst.NO_TAGS) List<String> tagsIds
) { }

