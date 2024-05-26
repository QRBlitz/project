package kz.iitu.diploma.project.config;

import kz.iitu.diploma.project.model.User;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditorAware implements AuditorAware<User> {

    @Override
    @NonNull
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            if (authentication.getPrincipal().equals("anonymousUser")) return Optional.empty();
            if (authentication.getPrincipal().getClass() == User.class) {
                return Optional.of((User) authentication.getPrincipal());
            }
        }
        return Optional.empty();
    }

}
