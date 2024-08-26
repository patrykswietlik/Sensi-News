package pl.sensi.news.tag;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.tag.api.models.Tag;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TagMapperJpa {
    List<Tag> toDomainList(List<TagEntity> tagEntityList);
    TagEntity toEntity(Tag tag);
    Tag toDomain(TagEntity tagEntity);
}

