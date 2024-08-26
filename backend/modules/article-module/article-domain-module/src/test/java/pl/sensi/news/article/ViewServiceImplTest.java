package pl.sensi.news.article;

import jakarta.servlet.http.HttpServletRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.View;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ViewServiceImplTest {

    @Mock
    private ViewRepository viewRepository;

    @InjectMocks
    private ViewServiceImpl viewService;

    @Mock
    private HttpServletRequest request;

    private ServletRequestAttributes requestAttributes;

    private final String mockedIp = "127.0.0.1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void handleView_shouldReturnTrueIfUserHasNotSeenArticleYet() {
        Article article = Instancio.create(Article.class);

        when(viewRepository.existsByArticleIdAndIpAddress(eq(article.id()), any(String.class))).thenReturn(false);
        when(request.getRemoteAddr()).thenReturn(mockedIp);

        boolean result = viewService.handleView(article.id());
        assertTrue(result);
        verify(viewRepository).save(any(View.class));
    }

    @Test
    public void handleView_shouldReturnFalseIfUserHasAlreadySeenArticle() {
        Article article = Instancio.create(Article.class);

        when(viewRepository.existsByArticleIdAndIpAddress(eq(article.id()), any(String.class))).thenReturn(true);
        when(request.getRemoteAddr()).thenReturn(mockedIp);

        boolean result = viewService.handleView(article.id());
        assertFalse(result);
        verify(viewRepository, never()).save(any(View.class));
    }

}