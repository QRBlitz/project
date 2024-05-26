package kz.iitu.diploma.project.repository;

import jakarta.transaction.Transactional;
import kz.iitu.diploma.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query(value = "select * from users where login = :login or phone = :login or email = :login", nativeQuery = true)
    User findByLogin(String login);

    @Transactional
    @Modifying
    @Query(value = "update users set last_login = now() where login = :login", nativeQuery = true)
    void updateLastLoginTime(String login);

    @Transactional
    @Modifying
    @Query(value = "update users set is_blocked = true where id = :id", nativeQuery = true)
    void blockUser(Long id);

    @Transactional
    @Modifying
    @Query(value = "update users set is_blocked = false where id = :id", nativeQuery = true)
    void unblockUser(Long id);

    Boolean existsByLoginAndBlockedIsFalse(String login);

}
