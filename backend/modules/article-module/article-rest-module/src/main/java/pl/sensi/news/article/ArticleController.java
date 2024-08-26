package pl.sensi.news.article;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.sensi.news.article.api.ArticleService;
import pl.sensi.news.article.api.CommentService;
import pl.sensi.news.article.api.RatingService;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleStatusSetForm;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.Rating;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = EndpointConst.ARTICLES)
@RequiredArgsConstructor
class ArticleController {

    private final ArticleService articleService;

    private final CommentService commentService;

    private final RatingService ratingService;

    @GetMapping
    public List<Article> getAll(Boolean withDeleted, Pageable pageable) {
        return articleService.getAll(withDeleted, pageable);
    }

    @GetMapping(EndpointConst.PENDING)
    public List<Article> getAllPending(Pageable pageable) {
        return articleService.getAllPending(pageable);
    }

    @GetMapping(EndpointConst.ID)
    public Article getById(@PathVariable String id) {
        return articleService.getById(id);
    }

    @GetMapping(EndpointConst.ARTICLES_SEARCH)
    public List<Article> search(@RequestParam List<String> tags) {
        return articleService.getByTags(tags);
    }

    @GetMapping(EndpointConst.ID + EndpointConst.COMMENTS)
    public List<Comment> getAllByArticleId(@PathVariable String id) {
        return commentService.getAllByArticleId(id);
    }

    @PostMapping(EndpointConst.ID + EndpointConst.LIKE)
    public ResponseEntity<ApplicationSuccessResponse<Rating>> rate(@PathVariable String id) {
        Rating createdRating = articleService.rate(id);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                createdRating,
                Objects.isNull(createdRating) ? SuccessCode.RATING_DELETED : SuccessCode.RATING_CREATED,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ApplicationSuccessResponse<Article>> create(@RequestBody ArticleRequestForm form) {
        Article createdArticle = articleService.create(form);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                createdArticle,
                SuccessCode.ARTICLE_CREATED,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(EndpointConst.ID)
    public ResponseEntity<ApplicationSuccessResponse<Objects>> delete(@PathVariable String id) {
        articleService.delete(id);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                null,
                SuccessCode.ARTICLE_DELETED,
                HttpStatus.OK
        );
    }

    @PutMapping(EndpointConst.ID)
    public ResponseEntity<ApplicationSuccessResponse<Article>> update(@PathVariable String id, @RequestBody ArticleRequestForm form) {
        Article updatedArticle = articleService.update(id, form);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                updatedArticle,
                SuccessCode.ARTICLE_UPDATED,
                HttpStatus.OK
        );
    }

    @PatchMapping(EndpointConst.ID + EndpointConst.STATUS)
    public ResponseEntity<ApplicationSuccessResponse<Article>> setStatus(@PathVariable String id, @RequestBody ArticleStatusSetForm status) {
        Article updatedArticle = articleService.setStatus(id, status);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                updatedArticle,
                SuccessCode.ARTICLE_UPDATED,
                HttpStatus.OK
        );
    }
}
