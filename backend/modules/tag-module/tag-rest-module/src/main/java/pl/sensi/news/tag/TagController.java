package pl.sensi.news.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sensi.news.article.ApplicationSuccessResponse;
import pl.sensi.news.article.EndpointConst;
import pl.sensi.news.article.ResponseEntityGenerator;
import pl.sensi.news.article.SuccessCode;
import pl.sensi.news.tag.api.TagService;
import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;
import pl.sensi.news.tag.api.models.TagsRequestForm;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = EndpointConst.TAGS)
@RequiredArgsConstructor
class TagController {
    private final TagService tagService;

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping(EndpointConst.ID)
    public Tag getById(@PathVariable String id) {
        return tagService.getById(id);
    }

    @PostMapping(EndpointConst.ALL)
    public List<Tag> getByIds(@RequestBody TagsRequestForm form) {
        return tagService.getByIds(form);
    }

    @PostMapping
    public ResponseEntity<ApplicationSuccessResponse<Tag>> create(@RequestBody TagSaveForm tag) {
        Tag createdTag = tagService.create(tag);
        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                createdTag,
                SuccessCode.TAG_CREATED,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(EndpointConst.ID)
    public ResponseEntity<ApplicationSuccessResponse<Objects>> delete(@PathVariable String id) {
        tagService.delete(id);

        return ResponseEntityGenerator.prepareSuccessResponseEntity(
                null,
                SuccessCode.TAG_DELETED,
                HttpStatus.OK
        );
    }
}
