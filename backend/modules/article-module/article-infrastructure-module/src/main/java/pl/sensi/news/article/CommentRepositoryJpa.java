package pl.sensi.news.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface CommentRepositoryJpa extends JpaRepository<CommentEntity, String> {

    List<CommentEntity> findAllByArticleIdAndReplyToIdIsNullOrderByCreatedAtDesc(String id);
    List<CommentEntity> findAllByReplyToIdOrderByCreatedAtDesc(String id);
}
