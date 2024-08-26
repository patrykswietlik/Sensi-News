package pl.sensi.news.tag.api.models;

import jakarta.validation.constraints.NotEmpty;
import pl.sensi.news.article.ValidationMessageConst;

import java.util.List;

public record TagsRequestForm(
        @NotEmpty(message = ValidationMessageConst.NO_TAGS) List<String> ids
) { }
