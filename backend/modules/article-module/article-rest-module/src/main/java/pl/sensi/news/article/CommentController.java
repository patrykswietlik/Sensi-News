package pl.sensi.news.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sensi.news.article.api.CommentService;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentReplyForm;
import pl.sensi.news.article.api.models.CommentSaveForm;

import java.util.Objects;

@RestController
@RequestMapping(value = EndpointConst.COMMENTS)
@RequiredArgsConstructor
class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApplicationSuccessResponse<Comment>> create(@RequestBody CommentSaveForm form) {
        Comment createdComment = commentService.create(form);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                createdComment,
                SuccessCode.COMMENT_CREATED,
                HttpStatus.CREATED
        );
    }

    @PostMapping(EndpointConst.COMMENTS_REPLY)
    public ResponseEntity<ApplicationSuccessResponse<Comment>> createReply(@RequestBody CommentReplyForm form) {
        Comment createdComment = commentService.create(form);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                createdComment,
                SuccessCode.COMMENT_CREATED,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(EndpointConst.ID)
    public ResponseEntity<ApplicationSuccessResponse<Objects>> delete(@PathVariable String id) {
        commentService.delete(id);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                null,
                SuccessCode.COMMENT_DELETED,
                HttpStatus.OK
        );
    }

    @PutMapping(EndpointConst.ID)
    public ResponseEntity<ApplicationSuccessResponse<Comment>> update(@PathVariable String id, @RequestBody CommentSaveForm form) {
        Comment updatedComment = commentService.update(id, form);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                updatedComment,
                SuccessCode.COMMENT_UPDATED,
                HttpStatus.OK
        );
    }
}
