package pl.sensi.news.tag.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pl.sensi.news.article.ValidationMessageConst;

public record TagSaveForm(
    @NotBlank(message = ValidationMessageConst.NO_NAME)
    @Size(max = 36, message = ValidationMessageConst.TAG_TOO_LONG)
    String engName,

    @NotBlank(message = ValidationMessageConst.NO_NAME)
    @Size(max = 36, message = ValidationMessageConst.TAG_TOO_LONG)
    String plName
) { }
