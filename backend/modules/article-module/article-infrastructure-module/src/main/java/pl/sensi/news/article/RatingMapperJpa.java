package pl.sensi.news.article;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.models.Rating;

@Component
@Mapper(componentModel = "spring")
public interface RatingMapperJpa {

    RatingEntity toEntity(Rating rating);
    Rating toDomain(RatingEntity ratingEntity);
}
