package pl.sensi.news.tag;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;

@Component
@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toDomain(TagSaveForm form);
}
