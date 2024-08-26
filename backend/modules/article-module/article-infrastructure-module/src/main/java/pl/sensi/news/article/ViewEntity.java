package pl.sensi.news.article;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "views")
@Entity
public class ViewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String articleId;

    private String userIpAddress;

    private LocalDateTime createdAt;

    @PrePersist
    private void generateDate() {
        createdAt = LocalDateTime.now();
    }

}
