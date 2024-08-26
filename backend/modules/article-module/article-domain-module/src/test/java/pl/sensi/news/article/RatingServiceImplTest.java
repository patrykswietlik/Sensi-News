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
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.Rating;
import pl.sensi.news.user.account.api.models.UserAccount;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

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
    void getCountByArticleId_shouldReturnCountOfRatings() {
        Article article = Instancio.create(Article.class);

        when(ratingRepository.getCountByArticleId(article.id())).thenReturn(article.likesCount());

        int result = ratingService.getCountByArticleId(article.id());
        assertEquals(article.likesCount(), result);
    }

    @Test
    public void rate_shouldReturnRatingIfArticleExistsAndIsNotAlreadyRated() {
        Article article = Instancio.create(Article.class);

        Rating expected = Instancio.of(Rating.class)
                .set(field(Rating::articleId), article.id())
                .set(field(Rating::userAccountId), userAccount.id())
                .create();

        when(ratingRepository.existsByIds(article.id(), userAccount.id())).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(ratingRepository.save(any(Rating.class))).thenReturn(expected);

        Rating result = ratingService.rate(article.id());
        assertEquals(expected, result);
        verify(ratingRepository, never()).deleteByArticleId(article.id());
    }

    @Test
    public void rate_shouldReturnNullIfArticleExistsAndIsAlreadyRated() {
        Article article = Instancio.create(Article.class);

        Rating expected = Instancio.of(Rating.class)
                .set(field(Rating::articleId), article.id())
                .set(field(Rating::userAccountId), userAccount.id())
                .create();

        when(ratingRepository.existsByIds(article.id(), userAccount.id())).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(ratingRepository.save(any(Rating.class))).thenReturn(expected);

        Rating result = ratingService.rate(article.id());
        assertNull(result);
        verify(ratingRepository).deleteByArticleId(article.id());
    }
}