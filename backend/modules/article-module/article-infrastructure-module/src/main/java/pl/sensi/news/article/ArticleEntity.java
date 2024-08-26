package pl.sensi.news.article;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.sensi.news.tag.TagEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "article")
@Entity
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String content;

    private String image;

    private String userAccountId;

    @ManyToMany
    @JoinTable(
            name = "article_tags",
            joinColumns = @JoinColumn(
                    name = "article_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "id"
            )
    )
    private List<TagEntity> tags;

    private int views;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    private void initialize() {
        createdAt = LocalDateTime.now();
        status = ArticleStatus.PENDING;
    }

}
