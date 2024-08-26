package pl.sensi.news.article;

import pl.sensi.news.article.api.models.View;

public interface ViewRepository {

    void save(View view);
    boolean existsByArticleIdAndIpAddress(String articleId, String userAccountId);
}
