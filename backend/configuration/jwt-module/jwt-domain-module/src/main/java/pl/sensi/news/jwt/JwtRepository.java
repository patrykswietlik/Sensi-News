package pl.sensi.news.jwt;

import pl.sensi.news.jwt.api.models.Jwt;

import java.util.List;

public interface JwtRepository {
    Jwt save(Jwt jwt);
    void deleteAllByUserAccountId(String id);
    boolean existsByTokenId(String id);
    List<Jwt> findAllByUserAccountId(String id);
    String getUserAccountIdFromEmail(String email);
}
