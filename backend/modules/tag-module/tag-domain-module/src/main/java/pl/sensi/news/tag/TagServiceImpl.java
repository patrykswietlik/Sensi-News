package pl.sensi.news.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sensi.news.article.ApplicationException;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.tag.api.TagService;
import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;
import pl.sensi.news.tag.api.models.TagsRequestForm;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    @Override
    public List<Tag> getAll() {
        return loadArticlesCountList(tagRepository.getAll());
    }

    @Override
    public Tag getById(String id) {
        Optional<Tag> optionalTag = tagRepository.getById(id);
        if (optionalTag.isEmpty()) {
            throw new ApplicationException(ErrorCode.TAG_NOT_FOUND);
        }

        return loadArticlesCount(optionalTag.get());
    }

    @Override
    public Tag create(TagSaveForm form) {
        Optional<Tag> tagOptional = tagRepository.getByNames(form);
        if (tagOptional.isPresent()) {
            Tag existingTag = tagOptional.get();
            if (existingTag.deletedOn() == null) {
                throw new ApplicationException(ErrorCode.TAG_ALREADY_EXISTS);
            }
            return tagRepository.save(existingTag.toBuilder().deletedOn(null).build());
        }
        return tagRepository.save(tagMapper.toDomain(form));
    }

    @Override
    public void delete(String id) {
        Tag tag = tagRepository.getByIdOrThrowException(id);

        if (!tag.removable()) {
            throw new ApplicationException(ErrorCode.TAG_CANNOT_BE_DELETED);
        }

        tagRepository.delete(id);
    }

    @Override
    public List<Tag> getByIds(TagsRequestForm form) {
        return loadArticlesCountList(tagRepository.getByIds(form.ids()));
    }

    private Tag loadArticlesCount(Tag tag) {
        return tag.toBuilder()
                .totalArticles(tagRepository.getArticleCount(tag))
                .build();
    }

    private List<Tag> loadArticlesCountList(List<Tag> tags) {
        return tags.stream()
                .map(this::loadArticlesCount)
                .toList();
    }
}
