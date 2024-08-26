package pl.sensi.news.article;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleUpdateForm;
import pl.sensi.news.tag.TagEntity;
import pl.sensi.news.tag.TagRepositoryJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleRepositoryJpa articleRepositoryJpa;

    private final TagRepositoryJpa tagRepositoryJpa;

    private final ArticleMapperJpa articleMapperJpa;

    @Override
    public List<Article> getAll(Boolean withDeleted, Pageable pageable) {
        Page<ArticleEntity> articleEntities = withDeleted ?
                articleRepositoryJpa.findAllApproved(pageable) :
                articleRepositoryJpa.findAllByTagsWithDeletedOnNullAndApproved(pageable);

        return articleEntities.stream()
                .map(articleMapperJpa::customToDomain)
                .toList();
    }

    @Override
    public List<Article> getAllPending(Pageable pageable) {
        Page<ArticleEntity> articleEntities = articleRepositoryJpa.findAllByStatusEqualsPending(pageable);

        return articleEntities.stream()
                .map(articleMapperJpa::customToDomain)
                .toList();
    }

    @Override
    public List<Article> getByTags(List<String> tags) {
        return articleMapperJpa.customMapToArticleList(articleRepositoryJpa.findByTagIds(tags));
    }

    @Transactional
    @Override
    public void addView(String id) {
        ArticleEntity articleEntity = articleRepositoryJpa.findById(id).get();
        articleRepositoryJpa.save(
                articleEntity.toBuilder()
                .views(articleEntity.getViews() + 1)
                .build()
        );
    }

    @Override
    public Article save(Article article, List<String> tagsIds){
        ArticleEntity articleEntity = articleMapperJpa.toEntity(article);
        articleEntity.setTags(mapTags(tagsIds));
        articleRepositoryJpa.save(articleEntity);

        return articleMapperJpa.customToDomain(articleEntity);
    }

    @Override
    public void delete(String id) {
        articleRepositoryJpa.deleteById(id);
    }

    @Override
    @Transactional
    public Article update(ArticleUpdateForm form) {
        ArticleEntity existingArticle = articleRepositoryJpa.findById(form.id()).get();

        articleMapperJpa.update(existingArticle, form.toBuilder()
                .image(Objects.isNull(form.image()) ? existingArticle.getImage() : form.image())
                .build());
        existingArticle.setTags(new ArrayList<>(mapTags(form.tags())));

        return articleMapperJpa.customToDomain(existingArticle);
    }

    @Override
    public List<String> validateTags(List<String> tagIds) {
        return tagRepositoryJpa.findAllByDeletedOnIsNullAndIdIn(tagIds).stream()
                .map(TagEntity::getId)
                .toList();
    }

    @Override
    public boolean existsById(String id) {
        return articleRepositoryJpa.existsById(id);
    }

    @Override
    public Article getByIdOrThrowException(String id) {
        ArticleEntity existingArticle = articleRepositoryJpa.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND));

        return articleMapperJpa.customToDomain(existingArticle);
    }

    @Override
    @Transactional
    public Article setStatus(String id, ArticleStatus status) {
        ArticleEntity existingArticle = articleRepositoryJpa.findById(id).get();
        articleRepositoryJpa.save(existingArticle.toBuilder().status(status).build());

        return articleMapperJpa.customToDomain(existingArticle);
    }

    private List<TagEntity> mapTags(List<String> tagIds) {
        return tagIds.stream()
                .map(tagRepositoryJpa::findById)
                .map(Optional::get)
                .toList();
    }
}
