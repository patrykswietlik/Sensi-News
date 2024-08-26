package pl.sensi.news.user.account;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_account")
@Entity
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private LocalDateTime createdAt;

    @PrePersist
    private void generateDate() {
        createdAt = LocalDateTime.now();
        role = RoleEnum.USER;
    }
}
