package pl.sensi.news.article;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sensi.news.article.api.ArticleService;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentReplyForm;
import pl.sensi.news.article.api.models.CommentSaveForm;
import pl.sensi.news.user.account.api.UserAccountService;
import pl.sensi.news.user.account.api.models.UserAccount;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleService articleService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UserAccount userAccount;

    private UserAccountResponse userAccountResponse;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        userAccount = Instancio.create(UserAccount.class);

        userAccountResponse = Instancio.of(UserAccountResponse.class)
                .set(field(UserAccountResponse::id), userAccount.id())
                .set(field(UserAccountResponse::email), userAccount.email())
                .set(field(UserAccountResponse::username), userAccount.username())
                .set(field(UserAccountResponse::role), userAccount.role())
                .create();

        userDetails = User.withUsername(userAccount.id())
                .password(userAccount.password())
                .authorities(userAccount.role())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Test
    public void getAllByArticleId_shouldReturnAllByArticleId() {
        Article article = Instancio.of(Article.class)
                .set(field(Article::userAccountId), userAccountResponse.id())
                .set(field(Article::userAccountUsername), userAccountResponse.username())
                .create();

        List<Comment> expected = Instancio.ofList(Comment.class).size(10).create();

        expected = expected.stream()
                .map(comment -> comment.toBuilder()
                        .userAccountId(userAccountResponse.id())
                        .userAccountUsername(userAccountResponse.username())
                        .build())
                .toList();

        when(articleService.existsById(article.id())).thenReturn(true);
        when(commentRepository.getAllByArticleId(article.id())).thenReturn(expected);
        when(userAccountService.getById(article.userAccountId())).thenAnswer(invocation -> userAccountResponse);
        when(commentRepository.getAllRepliesById(article.id())).thenReturn(List.of());

        List<Comment> result = commentService.getAllByArticleId(article.id());
        assertEquals(expected, result);
    }

    @Test
    public void getAllByArticleId_shouldThrowExceptionWhenArticleDoesNotExists() {
        Article article = Instancio.create(Article.class);

        when(articleService.existsById(article.id())).thenReturn(false);

        Exception result = assertThrows(ApplicationException.class, () -> commentService.getAllByArticleId(article.id()));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void create_shouldCreateCommentIfValidFormAndArticleExists() {
        CommentSaveForm form = Instancio.create(CommentSaveForm.class);

        Comment expected = Instancio.of(Comment.class)
                        .set(field(Comment::content), form.content())
                        .set(field(Comment::articleId), form.articleId())
                        .set(field(Comment::userAccountId), userAccountResponse.id())
                        .set(field(Comment::userAccountUsername), userAccountResponse.username())
                        .create();

        when(articleService.existsById(form.articleId())).thenReturn(true);
        when(commentMapper.commentSaveFormToComment(form)).thenReturn(expected);
        when(commentRepository.save(expected)).thenReturn(expected);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userAccountService.getById(expected.userAccountId())).thenAnswer(invocation -> userAccountResponse);

        Comment result = commentService.create(form);
        assertEquals(expected, result);
    }

    @Test
    public void create_shouldThrowExceptionIfArticleDoesNotExists() {
        Article article = Instancio.create(Article.class);
        CommentSaveForm form = Instancio.create(CommentSaveForm.class);

        when(articleService.existsById(article.id())).thenReturn(false);

        Exception result = assertThrows(ApplicationException.class, () -> commentService.create(form));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void createReply_shouldCreateReplyIfValidFormAndArticleExists() {
        Article article = Instancio.create(Article.class);

        Comment comment = Instancio.of(Comment.class)
                .set(field(Comment::articleId), article.id())
                .set(field(Comment::replyToId), null)
                .create();

        CommentReplyForm form = Instancio.of(CommentReplyForm.class)
                        .set(field(CommentReplyForm::articleId), article.id())
                        .set(field(CommentReplyForm::replyToId), comment.id())
                        .create();

        Comment expected = Instancio.of(Comment.class)
                        .set(field(Comment::articleId), article.id())
                        .set(field(Comment::replyToId), form.replyToId())
                        .set(field(Comment::userAccountId), userAccount.id())
                        .set(field(Comment::userAccountUsername), userAccount.username())
                        .create();

        when(articleService.existsById(form.articleId())).thenReturn(true);
        when(commentRepository.getByIdOrThrowException(form.replyToId())).thenReturn(comment);
        when(commentMapper.commentReplyFormToComment(form)).thenReturn(expected);
        when(commentRepository.save(expected)).thenReturn(expected);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userAccountService.getById(expected.userAccountId())).thenAnswer(invocation -> userAccountResponse);

        Comment result = commentService.create(form);
        assertEquals(expected, result);
    }

    @Test
    public void createReply_shouldThrowExceptionIfArticleDoesNotExists() {
        Article article = Instancio.create(Article.class);
        CommentReplyForm form = Instancio.create(CommentReplyForm.class);

        when(articleService.existsById(article.id())).thenReturn(false);

        Exception result = assertThrows(ApplicationException.class, () -> commentService.create(form));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void createReply_shouldThrowExceptionWhenReplyingOnReply() {
        CommentReplyForm form = Instancio.create(CommentReplyForm.class);
        Comment comment = Instancio.create(Comment.class);

        when(articleService.existsById(form.articleId())).thenReturn(true);
        when(commentRepository.getByIdOrThrowException(form.replyToId())).thenReturn(comment);

        Exception result = assertThrows(ApplicationException.class, () -> commentService.create(form));
        assertEquals(ErrorCode.CANNOT_REPLY_ON_REPLY.getMessage(), result.getMessage());
    }

    @Test
    public void delete_shouldDeleteCommentIfExistsAndAuthorized() {
        Comment comment = Instancio.of(Comment.class)
                        .set(field(Comment::replies), null)
                        .set(field(Comment::userAccountId), userAccount.id())
                        .create();

        when(commentRepository.getByIdOrThrowException(comment.id())).thenReturn(comment);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        doNothing().when(commentRepository).delete(any(String.class));

        assertDoesNotThrow(() -> commentService.delete(comment.id()));
    }

    @Test
    public void delete_shouldThrowIfCommentExistsButUnauthorized() {
        Comment comment = Instancio.of(Comment.class)
                .set(field(Comment::replies), null)
                .create();

        when(commentRepository.getByIdOrThrowException(comment.id())).thenReturn(comment);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        doNothing().when(commentRepository).delete(any(String.class));

        Exception result = assertThrows(ApplicationException.class, () -> commentService.delete(comment.id()));
        assertEquals(ErrorCode.UNAUTHORIZED.getMessage(), result.getMessage());
    }

    @Test
    public void update_shouldUpdateCommentIfExistsAndAuthorized() {
        Article article = Instancio.create(Article.class);

        CommentSaveForm form = Instancio.of(CommentSaveForm.class)
                .set(field(CommentSaveForm::articleId), article.id())
                .create();

        Comment comment = Instancio.of(Comment.class)
                .set(field(Comment::replies), null)
                .set(field(Comment::userAccountId), userAccount.id())
                .set(field(Comment::content), form.content())
                .set(field(Comment::userAccountUsername), userAccount.username())
                .create();

        Comment expected = comment.toBuilder().content(form.content()).build();

        when(commentRepository.getByIdOrThrowException(comment.id())).thenReturn(comment);
        when(articleService.existsById(article.id())).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(commentRepository.update(comment.id(), form)).thenReturn(expected);
        when(userAccountService.getById(expected.userAccountId())).thenAnswer(invocation -> userAccountResponse);

        Comment result = commentService.update(comment.id(), form);
        assertEquals(expected, result);
    }

    @Test
    public void update_shouldThrowIfCommentDoesNotExists() {
        Comment comment = Instancio.create(Comment.class);
        CommentSaveForm form = Instancio.create(CommentSaveForm.class);

        when(commentRepository.getByIdOrThrowException(comment.id())).thenThrow(new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND));

        Exception result = assertThrows(ApplicationException.class, () -> commentService.update(comment.id(), form));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }
}