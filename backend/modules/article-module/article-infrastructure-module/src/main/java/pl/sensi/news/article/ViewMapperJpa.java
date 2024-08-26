package pl.sensi.news.article;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.models.View;

@Component
@Mapper(componentModel = "spring")
public interface ViewMapperJpa {

    ViewEntity toEntity(View view);
    View toDomain(ViewEntity viewEntity);
}
