package kz.iitu.diploma.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.iitu.diploma.project.model.core.StatusEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends StatusEntity {

}
