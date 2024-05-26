package kz.iitu.diploma.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.iitu.diploma.project.model.core.CreateEntity;
import lombok.*;

@Entity
@Table(name = "verification_code")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode extends CreateEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "code", nullable = false)
    private String code;

}
