package kz.iitu.diploma.project.repository;

import kz.iitu.diploma.project.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByUserIdAndRoleId(Long userId, Long roleId);

}
