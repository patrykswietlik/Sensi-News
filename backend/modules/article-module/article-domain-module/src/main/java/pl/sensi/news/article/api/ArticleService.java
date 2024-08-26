package pl.sensi.news.article.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleStatusSetForm;
import pl.sensi.news.article.api.models.Rating;

import java.util.List;

@Validated
public interface ArticleService {

    List<Article> getAll(Boolean withDeleted, Pageable pageable);
    List<Article> getAllPending(Pageable pageable);
    Article getById(@NotBlank String id);
    List<Article> getByTags(List<String> tags);
    Rating rate(@NotBlank String id);
    Article create(@Valid ArticleRequestForm form);
    void delete(@NotBlank String id);
    Article update(@NotBlank String id, @Valid ArticleRequestForm form);
    boolean existsById(@NotBlank String id);
    Article setStatus(@NotBlank String id, @Valid ArticleStatusSetForm form);
}
