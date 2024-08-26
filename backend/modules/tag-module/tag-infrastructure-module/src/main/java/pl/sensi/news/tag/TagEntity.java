package pl.sensi.news.tag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sensi.news.article.ArticleStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tag")
@Entity
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String engName;

    @Column(nullable = false, unique = true)
    private String plName;

    private boolean removable;

    private LocalDateTime deletedOn;

    @PrePersist
    private void initialize() {
        this.removable = true;
    }
}
