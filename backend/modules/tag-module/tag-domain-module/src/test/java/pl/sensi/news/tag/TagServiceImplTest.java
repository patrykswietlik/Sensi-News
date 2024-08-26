package pl.sensi.news.tag;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sensi.news.article.ApplicationException;
import pl.sensi.news.article.ErrorCode;
import pl.sensi.news.tag.api.models.Tag;
import pl.sensi.news.tag.api.models.TagSaveForm;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getById_shouldReturnTagIfExists() {
        Tag expected = Instancio.create(Tag.class);

        when(tagRepository.getById(any(String.class))).thenReturn(Optional.of(expected));

        Tag result = tagService.getById(expected.id());
        assertEquals(expected.id(), result.id());
    }

    @Test
    public void getById_shouldThrowExceptionIfTagDoesNotExist() {
        when(tagRepository.getById(any(String.class))).thenReturn(Optional.empty());

        Exception result = assertThrows(ApplicationException.class, () -> tagService.getById(any(String.class)));
        assertEquals(ErrorCode.TAG_NOT_FOUND.getMessage(), result.getMessage());
    }

    @Test
    public void create_shouldCreateTagIfDoesNotAlreadyExists() {
        TagSaveForm form = Instancio.create(TagSaveForm.class);

        Tag expected = Instancio.of(Tag.class)
                .set(field(Tag::engName), form.engName())
                .set(field(Tag::plName), form.plName())
                .create();

        when(tagRepository.getByNames(form)).thenReturn(Optional.empty());
        when(tagMapper.toDomain(form)).thenReturn(expected);
        when(tagRepository.save(any(Tag.class))).thenReturn(expected);

        Tag result = tagService.create(form);
        assertEquals(expected, result);
    }

    @Test
    public void create_shouldThrowExceptionIfTagAlreadyExistsAndIsNotDeleted() {
        TagSaveForm form = Instancio.create(TagSaveForm.class);

        Tag existing = Instancio.of(Tag.class)
                .set(field(Tag::engName), form.engName())
                .set(field(Tag::plName), form.plName())
                .set(field(Tag::deletedOn), null)
                .create();

        when(tagRepository.getByNames(form)).thenReturn(Optional.of(existing));

        Exception result = assertThrows(ApplicationException.class, () -> tagService.create(form));
        assertEquals(ErrorCode.TAG_ALREADY_EXISTS.getMessage(), result.getMessage());
    }

    @Test
    public void create_shouldUpdateTagDeletedOnIfAlreadyExistsAndIsDeleted() {
        TagSaveForm form = Instancio.create(TagSaveForm.class);

        Tag existing = Instancio.of(Tag.class)
                .set(field(Tag::engName), form.engName())
                .set(field(Tag::plName), form.plName())
                .create();

        Tag expected = existing.toBuilder().deletedOn(null).build();

        when(tagRepository.getByNames(form)).thenReturn(Optional.of(existing));
        when(tagRepository.save(any(Tag.class))).thenReturn(expected);

        Tag result = tagService.create(form);
        assertEquals(expected, result);
    }

    @Test
    public void delete_shouldDeleteIfTagExists() {
        Tag existing = Instancio.of(Tag.class)
                .set(field(Tag::removable), true)
                .create();

        when(tagRepository.getByIdOrThrowException(existing.id())).thenReturn(existing);
        doNothing().when(tagRepository).delete(any(String.class));

        assertDoesNotThrow(() -> tagService.delete(existing.id()));
        verify(tagRepository).delete(existing.id());
    }

    @Test
    public void delete_shouldThrowExceptionIfTagDoesNotExist() {
        Tag tag = Instancio.create(Tag.class);

        when(tagRepository.getByIdOrThrowException(tag.id())).thenThrow(new ApplicationException(ErrorCode.TAG_NOT_FOUND));

        Exception result = assertThrows(ApplicationException.class, () -> tagService.delete(tag.id()));
        assertEquals(ErrorCode.TAG_NOT_FOUND.getMessage(), result.getMessage());
        verify(tagRepository, never()).delete(tag.id());
    }

}