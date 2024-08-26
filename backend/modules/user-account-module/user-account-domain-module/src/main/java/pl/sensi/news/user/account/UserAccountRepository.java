package pl.sensi.news.user.account;

import pl.sensi.news.user.account.api.models.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {
    Optional<UserAccount> getByEmail(String email);
    Optional<UserAccount> getById(String id);
    Optional<UserAccount> getByUsername(String username);
    UserAccount save(UserAccount userAccount);
}
