package pl.sensi.news.user.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserAccountRepositoryJpa extends JpaRepository<UserAccountEntity, String> {

    UserAccountEntity findByEmail(String email);
    Optional<UserAccountEntity> findById(String id);
    UserAccountEntity findByUsernameIgnoreCase(String email);
}
