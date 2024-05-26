package kz.iitu.diploma.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.iitu.diploma.project.model.core.UpdateEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "user_roles")
public class UserRole extends UpdateEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

}
