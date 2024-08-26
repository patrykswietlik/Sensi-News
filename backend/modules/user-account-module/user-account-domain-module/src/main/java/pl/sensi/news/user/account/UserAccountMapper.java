package pl.sensi.news.user.account;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import pl.sensi.news.user.account.api.models.UserAccount;
import pl.sensi.news.user.account.api.models.UserAccountRegisterForm;
import pl.sensi.news.user.account.api.models.UserAccountResponse;


@Component
@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccount toDomain(UserAccountRegisterForm form);
    UserAccountResponse toResponse(UserAccount userAccount);
}
