package pl.sensi.news.tag.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;
import pl.sensi.news.tag.api.models.TagsRequestForm;

import java.util.List;

@Validated
public interface TagService {

    List<Tag> getAll();
    Tag getById(@NotBlank String id);
    Tag create(@Valid TagSaveForm form);
    void delete(@NotBlank String id);
    List<Tag> getByIds(@Valid TagsRequestForm form);
}
