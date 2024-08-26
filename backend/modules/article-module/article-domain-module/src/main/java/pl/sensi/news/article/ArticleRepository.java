package pl.sensi.news.article;

import org.springframework.data.domain.Pageable;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleUpdateForm;

import java.util.List;

public interface ArticleRepository {

    List<Article> getAll(Boolean withDeleted, Pageable pageable);
    List<Article> getAllPending(Pageable pageable);
    List<Article> getByTags(List<String> tags);
    void addView(String id);
    Article save(Article article, List<String> tagsIds);
    void delete(String id);
    Article update(ArticleUpdateForm form);
    List<String> validateTags(List<String> tags);
    boolean existsById(String id);
    Article getByIdOrThrowException(String id);
    Article setStatus(String id, ArticleStatus status);
}
