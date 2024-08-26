package pl.sensi.news.user.account;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.user.account.api.models.UserAccount;

@Component
@Mapper(componentModel = "spring")
public interface UserAccountMapperJpa {
    UserAccount toDomain(UserAccountEntity userAccountEntity);
    UserAccountEntity toEntity(UserAccount userAccount);
}
