package kz.iitu.diploma.project.model.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.iitu.diploma.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UpdateEntity extends CreateEntity {

    @LastModifiedBy
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy = getCurrentUser();

    @LastModifiedDate
    @Column(name = "updated_date")
    private Date updatedDate;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            if (authentication.getPrincipal().equals("anonymousUser"))
                return null;
            if (authentication.getPrincipal().getClass() == User.class) {
                return (User) authentication.getPrincipal();
            }
        }
        return null;
    }

}
