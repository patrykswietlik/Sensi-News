package pl.sensi.news.article;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sensi.news.article.api.ArticleService;
import pl.sensi.news.article.api.CommentService;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentReplyForm;
import pl.sensi.news.article.api.models.CommentSaveForm;
import pl.sensi.news.security.SecurityUtils;
import pl.sensi.news.user.account.api.UserAccountService;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleService articleService;

    private final UserAccountService userAccountService;

    private final CommentMapper commentMapper;

    @Override
    public List<Comment> getAllByArticleId(String id) {
        if (!articleService.existsById(id)) {
            throw new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.getAllByArticleId(id);
        comments = loadRepliesByIds(comments);
        return loadUsernamesByUserAccountIds(comments);
    }

    @Override
    public Comment create(CommentSaveForm form) {
        if (!articleService.existsById(form.articleId())) {
            throw new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        Comment createdComment = commentRepository.save(
                commentMapper.commentSaveFormToComment(form).toBuilder().userAccountId(SecurityUtils.getLoggedInUserId()).build()
        );

        return loadUsernameByUserAccountId(createdComment);
    }

    @Override
    public Comment create(CommentReplyForm form) {
        if (!articleService.existsById(form.articleId())) {
            throw new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        Comment existingComment = commentRepository.getByIdOrThrowException(form.replyToId());

        if (!Objects.isNull(existingComment.replyToId())) {
            throw new ApplicationException(ErrorCode.CANNOT_REPLY_ON_REPLY);
        }

        Comment createdComment = commentRepository.save(
                commentMapper.commentReplyFormToComment(form).toBuilder().userAccountId(SecurityUtils.getLoggedInUserId()).build()
        );

        return loadUsernameByUserAccountId(createdComment);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Comment existingComment = commentRepository.getByIdOrThrowException(id);

        if (SecurityUtils.isNotAuthorized(existingComment.userAccountId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        List<Comment> replies = commentRepository.getAllRepliesById(id);
        replies.forEach(reply -> {
            commentRepository.delete(reply.id());
        });

        commentRepository.delete(id);
    }

    @Override
    public Comment update(String id, CommentSaveForm form) {
        Comment existingComment = commentRepository.getByIdOrThrowException(id);

        if (!articleService.existsById(form.articleId())) {
            throw new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        if (SecurityUtils.isNotAuthorized(existingComment.userAccountId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.update(id, form);
        existingComment = commentRepository.getByIdOrThrowException(id);

        return loadUsernameByUserAccountId(existingComment);
    }

    private Comment loadUsernameByUserAccountId(Comment comment) {
        UserAccountResponse userAccount = userAccountService.getById(comment.userAccountId());
        return comment.toBuilder().userAccountUsername(userAccount.username()).build();
    }

    private List<Comment> loadUsernamesByUserAccountIds(List<Comment> comments) {
        return comments.stream()
                .map(this::loadUsernameByUserAccountId)
                .toList();
    }

    private Comment loadRepliesById(Comment comment) {
        List<Comment> replies = commentRepository.getAllRepliesById(comment.id());
        replies = loadUsernamesByUserAccountIds(replies);

        return comment.toBuilder().replies(replies).build();
    }

    private List<Comment> loadRepliesByIds(List<Comment> comments) {
        return comments.stream()
                .map(this::loadRepliesById)
                .toList();
    }

}