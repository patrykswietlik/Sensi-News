package pl.sensi.news.article;

import pl.sensi.news.article.api.models.Rating;

public interface RatingRepository {
    int getCountByArticleId(String id);
    Rating save(Rating rating);
    void deleteByArticleId(String id);
    boolean existsByIds(String articleId, String userAccountId);
}
