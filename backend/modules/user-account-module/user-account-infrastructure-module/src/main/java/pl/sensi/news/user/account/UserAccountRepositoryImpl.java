package pl.sensi.news.user.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.sensi.news.user.account.api.models.UserAccount;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountRepositoryJpa userAccountRepositoryJpa;

    private final UserAccountMapperJpa userAccountMapperJpa;

    @Override
    public Optional<UserAccount> getByEmail(String email) {
        UserAccountEntity existingUserAccount = userAccountRepositoryJpa.findByEmail(email);
        return Optional.ofNullable(userAccountMapperJpa.toDomain(existingUserAccount));
    }

    @Override
    public Optional<UserAccount> getById(String id) {
        Optional<UserAccountEntity> existingUserAccount = userAccountRepositoryJpa.findById(id);
        return Optional.ofNullable(userAccountMapperJpa.toDomain(existingUserAccount.get()));
    }

    @Override
    public Optional<UserAccount> getByUsername(String username) {
        UserAccountEntity existingUserAccount = userAccountRepositoryJpa.findByUsernameIgnoreCase(username);
        return Optional.ofNullable(userAccountMapperJpa.toDomain(existingUserAccount));
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        UserAccountEntity userAccountEntity = userAccountMapperJpa.toEntity(userAccount);
        userAccountRepositoryJpa.save(userAccountEntity);
        return userAccountMapperJpa.toDomain(userAccountEntity);
    }

}
