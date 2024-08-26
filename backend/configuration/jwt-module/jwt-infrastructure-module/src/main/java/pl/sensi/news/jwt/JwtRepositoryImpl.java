package pl.sensi.news.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.sensi.news.jwt.api.models.Jwt;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JwtRepositoryImpl implements JwtRepository {

    private final JwtRepositoryJpa jwtRepositoryJpa;

    private final JwtMapperJpa jwtMapperJpa;

    @Override
    public Jwt save(Jwt jwt) {
        JwtEntity savedToken = jwtRepositoryJpa.save(jwtMapperJpa.jwtToJwtEntity(jwt));
        return jwtMapperJpa.jwtEntityToJwt(savedToken);
    }

    @Override
    public void deleteAllByUserAccountId(String id) {
        jwtRepositoryJpa.deleteAllByUserAccountId(id);
    }

    @Override
    public boolean existsByTokenId(String id) {
        return jwtRepositoryJpa.findByTokenId(id).isPresent();
    }

    @Override
    public List<Jwt> findAllByUserAccountId(String id) {
        return jwtMapperJpa.jwtEntityListToJwtList(jwtRepositoryJpa.getAllByUserAccountId(id));
    }

    @Override
    public String getUserAccountIdFromEmail(String email) {
        return jwtRepositoryJpa.getUserAccountIdFromEmail(email);
    }
}
