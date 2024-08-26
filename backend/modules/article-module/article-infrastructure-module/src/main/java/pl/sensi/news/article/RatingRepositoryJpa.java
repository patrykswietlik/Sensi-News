package pl.sensi.news.article;

import org.springframework.data.jpa.repository.JpaRepository;

interface RatingRepositoryJpa extends JpaRepository<RatingEntity, String> {

    int countByArticleId(String articleId);
    boolean existsByArticleIdAndUserAccountId(String articleId, String userAccountId);
    void deleteByArticleId(String id);
}
