package pl.sensi.news.article;

import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentSaveForm;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAllByArticleId(String id);
    List<Comment> getAllRepliesById(String id);
    Comment save(Comment comment);
    void delete(String id);
    Comment update(String id, CommentSaveForm form);
    Comment getByIdOrThrowException(String id);
    boolean existsById(String id);
}
