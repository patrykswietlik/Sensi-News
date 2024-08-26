package pl.sensi.news.article;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "rating")
@Entity
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String articleId;

    private String userAccountId;

    private LocalDateTime createdAt;

    @PrePersist
    private void generateDate() {
        createdAt = LocalDateTime.now();
    }
}
