package pl.sensi.news.article;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import pl.sensi.news.article.api.models.Comment;
import pl.sensi.news.article.api.models.CommentSaveForm;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapperJpa {

    CommentEntity toEntity(Comment comment);
    Comment toDomain(CommentEntity commentEntity);
    List<Comment> toDomainList(List<CommentEntity> commentEntityList);
    void update(@MappingTarget CommentEntity commentEntity, CommentSaveForm form);
}
