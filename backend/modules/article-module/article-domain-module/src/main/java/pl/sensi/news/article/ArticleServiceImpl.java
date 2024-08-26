package pl.sensi.news.article;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import pl.sensi.news.article.api.ArticleService;
import pl.sensi.news.article.api.ImageStorage;
import pl.sensi.news.article.api.RatingService;
import pl.sensi.news.article.api.ViewService;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleStatusSetForm;
import pl.sensi.news.article.api.models.ArticleUpdateForm;
import pl.sensi.news.article.api.models.Rating;
import pl.sensi.news.security.SecurityUtils;
import pl.sensi.news.user.account.api.UserAccountService;
import pl.sensi.news.user.account.api.models.UserAccountResponse;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final UserAccountService userAccountService;

    private final RatingService ratingService;

    private final ArticleMapper articleMapper;

    private final ViewService viewService;

    private final ImageStorage imageStorage;

    @Override
    public List<Article> getAll(Boolean withDeleted, Pageable pageable) {
        return loadArticlesDetails(articleRepository.getAll(withDeleted, pageable));
    }

    @Override
    public List<Article> getAllPending(Pageable pageable) {
        return loadArticlesDetails(articleRepository.getAllPending(pageable));
    }

    @Override
    public Article getById(String id) {
        Article existingArticle = articleRepository.getByIdOrThrowException(id);
        existingArticle = loadLikesCountById(existingArticle);

        if (!existingArticle.status().equals(ArticleStatus.APPROVED.name())) {
            if (SecurityUtils.isNotAuthorized(existingArticle.userAccountId())) {
                throw new ApplicationException(ErrorCode.UNAUTHORIZED);
            }
        } else {
            if (viewService.handleView(id)) {
                articleRepository.addView(id);
            }
        }

        return loadUsernameByUserAccountId(existingArticle);
    }

    @Override
    public List<Article> getByTags(List<String> tags) {
        return loadArticlesDetails(articleRepository.getByTags(tags));
    }

    @Override
    public Rating rate(String id) {
        if (!existsById(id)) {
            throw new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        return ratingService.rate(id);
    }

    @Override
    public Article create(ArticleRequestForm form) {
        if (Objects.isNull(form.image())) {
            throw new ConstraintViolationException(ValidationMessageConst.NO_IMAGE, null);
        }

        List<String> validatedTags = articleRepository.validateTags(form.tagsIds());
        if (validatedTags.isEmpty()) {
            throw new ApplicationException(ErrorCode.ARTICLE_HAS_NO_TAGS);
        }

        String url = imageStorage.storeImage(form.image());

        Article createdArticle = articleRepository.save(
                articleMapper.toDomain(form).toBuilder().userAccountId(SecurityUtils.getLoggedInUserId()).image(url).build(),
                validatedTags
        );

        return loadUsernameByUserAccountId(createdArticle);
    }

    @Override
    public void delete(String id) {
        checkIfExistsAndAuthorized(id);

        articleRepository.delete(id);
    }

    @Override
    public Article update(String id, ArticleRequestForm form) {
        checkIfExistsAndAuthorized(id);

        List<String> validatedTags = articleRepository.validateTags(form.tagsIds());
        if (validatedTags.isEmpty()) {
            throw new ApplicationException(ErrorCode.ARTICLE_HAS_NO_TAGS);
        }

        String url = Objects.isNull(form.image()) ? null : imageStorage.storeImage(form.image());
        Article existingArticle = loadUsernameByUserAccountId(articleRepository.update(new ArticleUpdateForm(id, form.title(), form.content(), validatedTags, url)));

        return loadUsernameByUserAccountId(existingArticle);
    }

    @Override
    public boolean existsById(String id) {
        return articleRepository.existsById(id);
    }

    @Override
    public Article setStatus(String id, ArticleStatusSetForm form) {
        checkIfExistsAndAuthorized(id);
        validateArticleStatus(form.status());

        return articleRepository.setStatus(id, ArticleStatus.valueOf(form.status()));
    }

    private void checkIfExistsAndAuthorized(String id) {
        Article existingArticle = articleRepository.getByIdOrThrowException(id);

        if (SecurityUtils.isNotAuthorized(existingArticle.userAccountId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void validateArticleStatus(String status) {
        if (!ObjectUtils.containsConstant(ArticleStatus.values(), status, true)) {
            throw new ApplicationException(ErrorCode.ARTICLE_STATUS_NOT_FOUND);
        }
    }


    private Article loadUsernameByUserAccountId(Article article) {
        UserAccountResponse userAccount = userAccountService.getById(article.userAccountId());
        return article.toBuilder().userAccountUsername(userAccount.username()).build();
    }

    private Article loadLikesCountById(Article article) {
        int likesCount = ratingService.getCountByArticleId(article.id());
        return article.toBuilder().likesCount(likesCount).build();
    }

    private List<Article> loadArticlesDetails(List<Article> articles) {
        return articles.stream()
                .map(this::loadUsernameByUserAccountId)
                .map(this::loadLikesCountById)
                .toList();
    }
}
