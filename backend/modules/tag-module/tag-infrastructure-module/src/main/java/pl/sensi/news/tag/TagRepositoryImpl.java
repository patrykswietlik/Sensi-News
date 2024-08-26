package pl.sensi.news.tag;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.sensi.news.article.ApplicationException;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TagRepositoryImpl implements TagRepository {

    private final TagRepositoryJpa tagRepositoryJpa;

    private final TagMapperJpa tagMapperJpa;

    @Override
    public List<Tag> getAll() {
        return tagMapperJpa.toDomainList(tagRepositoryJpa.findAll());
    }

    @Override
    public Optional<Tag> getById(String id) {
        TagEntity existingTag = tagRepositoryJpa.findById(id).orElse(null);
        return Optional.ofNullable(tagMapperJpa.toDomain(existingTag));
    }

    @Override
    public Tag getByIdOrThrowException(String id) {
        return tagMapperJpa.toDomain(tagRepositoryJpa.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.TAG_NOT_FOUND)));
    }

    @Override
    public Optional<Tag> getByNames(TagSaveForm form) {
        TagEntity existingTag = tagRepositoryJpa.findByEngNameOrPlName(form.engName(), form.plName());
        return Optional.ofNullable(tagMapperJpa.toDomain(existingTag));
    }

    @Override
    public List<Tag> getByIds(List<String> ids) {
        return tagMapperJpa.toDomainList(tagRepositoryJpa.findAllByIdIn(ids));
    }

    @Override
    public int getArticleCount(Tag tag) {
        return tagRepositoryJpa.findArticleCountById(tag.id());
    }

    @Override
    @Transactional
    public Tag save(Tag tag) {
        TagEntity tagEntity = tagMapperJpa.toEntity(tag);
        tagRepositoryJpa.save(tagEntity);
        return tagMapperJpa.toDomain(tagEntity);
    }

    @Override
    public boolean existsById(String id) {
        return tagRepositoryJpa.findById(id).isPresent();
    }

    @Override
    @Transactional
    public void delete(String id) {
        TagEntity tag = tagRepositoryJpa.findById(id).get();
        tag.setDeletedOn(LocalDateTime.now());
    }
}
