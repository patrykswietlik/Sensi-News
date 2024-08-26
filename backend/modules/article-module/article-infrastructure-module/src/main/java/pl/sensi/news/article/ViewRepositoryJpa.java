package pl.sensi.news.article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepositoryJpa extends JpaRepository<ViewEntity, String> {

    boolean existsByArticleIdAndUserIpAddress(String articleId, String userAccountId);
}
