package pl.sensi.news.jwt;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.jwt.api.models.Jwt;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface JwtMapperJpa {
    JwtEntity jwtToJwtEntity(Jwt jwt);
    Jwt jwtEntityToJwt(JwtEntity jwtEntity);
    List<Jwt> jwtEntityListToJwtList(List<JwtEntity> jwtEntities);
}
