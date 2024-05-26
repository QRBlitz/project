package kz.iitu.diploma.project.repository;

import kz.iitu.diploma.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Role findByCode(String code);

    @Query("select name from Role where isActive = true")
    List<String> getRoleNames();
}
