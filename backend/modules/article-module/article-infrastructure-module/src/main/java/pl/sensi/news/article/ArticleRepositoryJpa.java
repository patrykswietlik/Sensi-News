package pl.sensi.news.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface ArticleRepositoryJpa extends JpaRepository<ArticleEntity, String> {
    @Query(
            "select a from ArticleEntity a join a.tags t where t.id in :tags and a.status = 'APPROVED'"
    )
    List<ArticleEntity> findByTagIds(@Param("tags") List<String> ids);

    @Query(value = "select a from ArticleEntity a where a.status = 'APPROVED'")
    Page<ArticleEntity> findAllApproved(Pageable pageable);

    @Query(value = "select distinct a.* from article a " +
            "join article_tags at on a.id = at.article_id " +
            "join tag t on at.tag_id = t.id " +
            "where t.deleted_on is null and a.status = 'APPROVED'", nativeQuery = true)
    Page<ArticleEntity> findAllByTagsWithDeletedOnNullAndApproved(Pageable pageable);

    @Query(value = "select distinct a.* from article a " +
            "join article_tags at on a.id = at.article_id " +
            "join tag t on at.tag_id = t.id " +
            "where t.deleted_on is null and a.status = 'PENDING'", nativeQuery = true)
    Page<ArticleEntity> findAllByStatusEqualsPending(Pageable pageable);
}
