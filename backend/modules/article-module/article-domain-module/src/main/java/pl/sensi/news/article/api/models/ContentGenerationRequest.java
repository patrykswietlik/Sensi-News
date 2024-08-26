package pl.sensi.news.article.api.models;

import jakarta.validation.constraints.NotBlank;

public record ContentGenerationRequest(
        @NotBlank String input
) { }
