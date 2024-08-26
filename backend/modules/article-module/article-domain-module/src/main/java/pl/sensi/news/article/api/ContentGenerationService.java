package pl.sensi.news.article.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import pl.sensi.news.article.api.models.ContentGenerationRequest;

@Validated
public interface ContentGenerationService {

    String generate(@Valid ContentGenerationRequest request, @NotBlank String lang);
}
