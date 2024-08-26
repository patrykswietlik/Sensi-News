package pl.sensi.news.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepositoryJpa extends JpaRepository<TagEntity, String> {
    List<TagEntity> findAllByDeletedOnIsNullAndIdIn(List<String> ids);
    Optional<TagEntity> findById(String id);

    @Query("select t from TagEntity t where upper(t.engName) = upper(:engName) or upper(t.plName) = upper(:plName)")
    TagEntity findByEngNameOrPlName(@Param("engName") String engName, @Param("plName") String plName);

    @Query(value = "select count(*) from article_tags t where t.tag_id = :id", nativeQuery = true)
    int findArticleCountById(String id);

    List<TagEntity> findAllByIdIn(List<String> ids);
}
