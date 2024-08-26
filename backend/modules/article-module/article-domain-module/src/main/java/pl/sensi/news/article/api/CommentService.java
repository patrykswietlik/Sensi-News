package pl.sensi.news.article.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentReplyForm;
import pl.sensi.news.article.api.models.CommentSaveForm;

import java.util.List;

@Validated
public interface CommentService {

    List<Comment> getAllByArticleId(@NotBlank String id);
    Comment create(@Valid CommentSaveForm form);
    Comment create(@Valid CommentReplyForm form);
    void delete(@NotBlank String id);
    Comment update(@NotBlank String id, @Valid CommentSaveForm form);
}
