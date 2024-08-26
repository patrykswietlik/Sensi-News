package pl.sensi.news.tag;

import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;

import java.util.List;
import java.util.Optional;

interface TagRepository {

    List<Tag> getAll();
    Optional<Tag> getById(String id);
    Tag getByIdOrThrowException(String id);
    Optional<Tag> getByNames(TagSaveForm form);
    List<Tag> getByIds(List<String> ids);
    int getArticleCount(Tag tag);
    Tag save(Tag tag);
    boolean existsById(String id);
    void delete(String id);

}
