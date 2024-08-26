package pl.sensi.news.article.api;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import pl.sensi.news.article.api.models.Rating;

@Validated
public interface RatingService {
    int getCountByArticleId(@NotBlank String id);
    Rating rate(String id);
}
