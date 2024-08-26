package pl.sensi.news.article;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentReplyForm;
import pl.sensi.news.article.api.models.CommentSaveForm;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment commentSaveFormToComment(CommentSaveForm comment);
    Comment commentReplyFormToComment(CommentReplyForm comment);
}
