package pl.sensi.news.article;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.models.Article;
import pl.sensi.news.article.api.models.ArticleRequestForm;

@Component
@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "image", ignore = true)
    Article toDomain(ArticleRequestForm form);
}