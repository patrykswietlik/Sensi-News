package pl.sensi.news.article;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentSaveForm;

import java.util.List;

@Repository
@RequiredArgsConstructor
class CommentRepositoryImpl implements CommentRepository {

    private final CommentRepositoryJpa commentRepositoryJpa;

    private final CommentMapperJpa commentMapperJpa;

    @Override
    public List<Comment> getAllByArticleId(String id) {
        return commentMapperJpa.toDomainList(commentRepositoryJpa.findAllByArticleIdAndReplyToIdIsNullOrderByCreatedAtDesc(id));
    }

    @Override
    public List<Comment> getAllRepliesById(String id) {
        return commentMapperJpa.toDomainList(commentRepositoryJpa.findAllByReplyToIdOrderByCreatedAtDesc(id));
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity createdComment = commentRepositoryJpa.save(commentMapperJpa.toEntity(comment));
        return commentMapperJpa.toDomain(createdComment);
    }

    @Override
    public void delete(String id) {
        commentRepositoryJpa.deleteById(id);
    }

    @Override
    @Transactional
    public Comment update(String id, CommentSaveForm form) {
        CommentEntity existingComment = commentRepositoryJpa.findById(id).get();
        commentMapperJpa.update(existingComment, form);
        return commentMapperJpa.toDomain(existingComment);
    }

    @Override
    public Comment getByIdOrThrowException(String id) {
        CommentEntity existingComment = commentRepositoryJpa.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));
        return commentMapperJpa.toDomain(existingComment);
    }

    @Override
    public boolean existsById(String id) {
        return commentRepositoryJpa.existsById(id);
    }
}
