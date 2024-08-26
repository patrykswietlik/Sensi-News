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
import org.springframework.test.util.ReflectionTestUtils;
import pl.sensi.news.article.api.ImageStorage;
import pl.sensi.news.article.api.RatingService;
import pl.sensi.news.article.api.ViewService;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleUpdateForm;
import pl.sensi.news.article.api.models.Rating;
import pl.sensi.news.user.account.api.UserAccountService;
import pl.sensi.news.user.account.api.models.UserAccount;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private RatingService ratingService;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private ViewService viewService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private ImageStorage imageStorage;

    @InjectMocks
    private ArticleServiceImpl articleService;

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
    public void getById_shouldReturnExistingArticle() {
        Article expected = Instancio.of(Article.class)
                .set(field(Article::userAccountId), userAccount.id())
                .set(field(Article::userAccountUsername), userAccount.username())
                .create();

        UserAccountResponse userAccountResponse = new UserAccountResponse(
                userAccount.id(),
                userAccount.email(),
                userAccount.username(),
                userAccount.role()
        );

        when(articleRepository.getByIdOrThrowException(expected.id()))
                .thenReturn(expected);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userAccountService.getById(expected.userAccountId()))
                .thenReturn(userAccountResponse);
        when(ratingService.getCountByArticleId(expected.id())).thenReturn(expected.likesCount());

        Article result = articleService.getById(expected.id());
        assertEquals(expected, result);
    }

    @Test
    public void getById_shouldThrowExceptionIfArticleNotFound() {
        Article article = Instancio.create(Article.class);

        when(articleRepository.getByIdOrThrowException(article.id()))
                .thenThrow(new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND));

        Exception result = assertThrows(ApplicationException.class, () -> articleService.getById(article.id()));

        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void rate_shouldReturnRatingIfArticleExists() {
        Article article = Instancio.of(Article.class)
                .set(field(Article::userAccountId), userAccount.id())
                .create();

        Rating expected = Instancio.of(Rating.class)
                        .set(field(Rating::articleId), article.id())
                        .set(field(Rating::userAccountId), userAccount.id())
                        .create();


        when(articleRepository.existsById(article.id())).thenReturn(true);
        when(ratingService.rate(article.id())).thenReturn(expected);

        Rating result = articleService.rate(article.id());
        assertEquals(expected, result);
    }

    @Test
    public void rate_shouldThrowExceptionIfArticleNotFound() {
        Article article = Instancio.of(Article.class)
                .set(field(Article::userAccountId), userAccount.id())
                .create();

        Rating rating = Instancio.of(Rating.class)
                .set(field(Rating::articleId), article.id())
                .set(field(Rating::userAccountId), userAccount.id())
                .create();


        when(articleRepository.existsById(article.id())).thenReturn(false);
        when(ratingService.rate(article.id())).thenReturn(rating);

        Exception result = assertThrows(ApplicationException.class, () -> articleService.rate(article.id()));

        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void create_shouldCreateArticle() {
        ArticleRequestForm form = Instancio.create(ArticleRequestForm.class);

        String imageUrl = Instancio.create(String.class);

        Article expected = Instancio.of(Article.class)
                .set(field(Article::title), form.title())
                .set(field(Article::userAccountId), userAccount.id())
                .set(field(Article::userAccountUsername), userAccount.username())
                .set(field(Article::content), form.content())
                .set(field(Article::image), imageUrl)
                .set(field(Article::tagsIds), form.tagsIds())
                .create();

        when(articleRepository.validateTags(form.tagsIds()))
                .thenReturn(form.tagsIds());
        when(userAccountService.getById(userAccount.id())).thenReturn(userAccountResponse);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(imageStorage.storeImage(any(byte[].class))).thenReturn(imageUrl);
        when(articleMapper.toDomain(any(ArticleRequestForm.class))).thenReturn(expected);
        when(articleRepository.save(any(Article.class), anyList())).thenAnswer(invocation -> invocation.<Article>getArgument(0));

        Article result = articleService.create(form);

        assertEquals(expected, result);
    }

    @Test
    public void create_shouldThrowExceptionIfArticleHasNoTags() {
        ArticleRequestForm form = Instancio.of(ArticleRequestForm.class)
                .set(field(ArticleRequestForm::tagsIds), List.of())
                .create();

        Exception result = assertThrows(ApplicationException.class, () -> articleService.create(form));

        assertEquals(ErrorCode.ARTICLE_HAS_NO_TAGS.getMessage(), result.getMessage());
    }

    @Test
    public void delete_shouldDeleteExistingArticleWhenAuthorized() {
        UserAccount authorAccount = Instancio.create(UserAccount.class);
        UserAccount adminAccount = Instancio.of(UserAccount.class)
                .set(field(UserAccount::role), AuthorityUserType.ADMIN.name())
                .create();

        UserDetails userDetails = User.withUsername(authorAccount.id())
                .password(authorAccount.password())
                .authorities(authorAccount.role())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

        UserDetails adminDetails = User.withUsername(adminAccount.id())
                .password(adminAccount.password())
                .authorities(adminAccount.role())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

        Article article = Instancio.of(Article.class)
                        .set(field(Article::userAccountId), authorAccount.id())
                        .create();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(articleRepository.getByIdOrThrowException(article.id())).thenReturn(article);

        assertDoesNotThrow(() -> articleService.delete(article.id()));

        when(authentication.getPrincipal()).thenReturn(adminDetails);

        assertDoesNotThrow(() -> articleService.delete(article.id()));
    }

    @Test
    public void delete_shouldThrowExceptionIfNotAuthorized() {
        Article article = Instancio.create(Article.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(articleRepository.getByIdOrThrowException(article.id())).thenReturn(article);

        Exception expected = assertThrows(ApplicationException.class, () -> articleService.delete(article.id()));
        assertEquals(ErrorCode.UNAUTHORIZED.getMessage(), expected.getMessage());
    }

    @Test
    public void update_shouldUpdateExistingArticleWhenAuthorized() {
        ArticleRequestForm form = Instancio.create(ArticleRequestForm.class);

        String imageUrl = Instancio.create(String.class);

        Article article = Instancio.of(Article.class)
                .set(field(Article::userAccountId), userAccount.id())
                .create();

        Article expected = article.toBuilder()
                .id(article.id())
                .title(form.title())
                .userAccountId(userAccount.id())
                .userAccountUsername(userAccount.username())
                .content(form.content())
                .image(imageUrl)
                .createdAt(article.createdAt())
                .updatedAt(article.updatedAt())
                .tagsIds(form.tagsIds())
                .views(article.views())
                .likesCount(article.likesCount())
                .build();

        when(articleRepository.getByIdOrThrowException(article.id())).thenReturn(article);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(articleRepository.validateTags(form.tagsIds())).thenReturn(form.tagsIds());
        when(imageStorage.storeImage(any(byte[].class))).thenReturn(imageUrl);
        when(userAccountService.getById(userAccount.id())).thenReturn(userAccountResponse);
        when(articleRepository.update(any(ArticleUpdateForm.class))).thenReturn(expected);

        Article result = articleService.update(article.id(), form);
        assertEquals(expected, result);
    }

    @Test
    public void update_shouldThrowExceptionIfArticleDoesNotExists() {
        Article article = Instancio.create(Article.class);
        ArticleRequestForm form = Instancio.create(ArticleRequestForm.class);

        when(articleRepository.getByIdOrThrowException(article.id()))
                .thenThrow(new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND));

        Exception result = assertThrows(ApplicationException.class, () -> articleService.update(article.id(), form));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void update_shouldThrowExceptionIfArticleHasNoTags() {
        Article article = Instancio.of(Article.class)
                .set(field(Article::userAccountId), userAccount.id())
                .create();

        ArticleRequestForm form = Instancio.create(ArticleRequestForm.class);

        when(articleRepository.getByIdOrThrowException(article.id())).thenReturn(article);
        when(articleRepository.validateTags(form.tagsIds())).thenReturn(List.of());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Exception result = assertThrows(ApplicationException.class, () -> articleService.update(article.id(), form));

        assertEquals(ErrorCode.ARTICLE_HAS_NO_TAGS.getMessage(), result.getMessage());
    }

    @Test
    public void loadArticlesDetails_shouldLoadArticlesDetails() {
        List<Article> articles = Instancio.ofList(Article.class)
                .size(10)
                .create();

        articles = articles.stream()
                .map(article -> article.toBuilder()
                        .userAccountId(userAccount.id())
                        .likesCount(0)
                        .build())
                .toList();

        List<Article> expected = articles.stream()
                .map(article -> article.toBuilder()
                        .userAccountUsername(userAccount.username())
                        .likesCount(5)
                        .build())
                .toList();

        when(userAccountService.getById(userAccount.id())).thenReturn(userAccountResponse);
        when(ratingService.getCountByArticleId(any(String.class))).thenReturn(5);

        List<Article> result = ReflectionTestUtils.invokeMethod(articleService, "loadArticlesDetails", articles);
        assertEquals(expected, result);

    }

}