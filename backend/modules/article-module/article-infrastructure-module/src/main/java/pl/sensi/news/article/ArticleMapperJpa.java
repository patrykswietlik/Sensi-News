package pl.sensi.news.article;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;
import pl.sensi.news.article.api.models.ArticleUpdateForm;
import pl.sensi.news.tag.TagEntity;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class ArticleMapperJpa {

    @Named("defaultMapping")
    @Mapping(target = "tagsIds", ignore = true)
    public abstract Article toDomain(ArticleEntity articleEntity);

    @Named("customMapping")
    @Mapping(source = "tags", target = "tagsIds", qualifiedByName = "extractTagsIds")
    public abstract Article customToDomain(ArticleEntity articleEntity);

    @Named("customMappingList")
    public List<Article> customMapToArticleList(List<ArticleEntity> articleEntities) {
        return articleEntities.stream()
                .map(this::customToDomain)
                .toList();
    }

    @Named("extractTagsIds")
    protected List<String> extractTagsIds(List<TagEntity> tags) {
        return tags.stream()
                .map(TagEntity::getId)
                .toList();
    }

    @IterableMapping(qualifiedByName = "defaultMapping")
    public abstract List<Article> articleEntityListToArticleList(List<ArticleEntity> articleEntityList);

    public abstract ArticleEntity toEntity(Article article);

    @Mapping(target = "tags", ignore = true)
    abstract void update(@MappingTarget ArticleEntity articleEntity, ArticleUpdateForm form);
}
