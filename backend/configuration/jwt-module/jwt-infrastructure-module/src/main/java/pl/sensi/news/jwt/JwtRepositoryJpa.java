package pl.sensi.news.jwt;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JwtRepositoryJpa extends JpaRepository<JwtEntity, String> {

    @Modifying
    @Transactional
    @Query("delete from JwtEntity j where j.userAccountId = :id")
    void deleteAllByUserAccountId(@Param("id") String id);

    @Query(value = "select u.id from user_account u where u.email = :email ", nativeQuery = true)
    String getUserAccountIdFromEmail(@Param("email") String email);

    List<JwtEntity> getAllByUserAccountId(String id);
    Optional<JwtEntity> findByTokenId(String id);
}
