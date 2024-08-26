package pl.sensi.news.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jwt")
@Entity
public class JwtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String tokenId;

    private String userAccountId;
}
