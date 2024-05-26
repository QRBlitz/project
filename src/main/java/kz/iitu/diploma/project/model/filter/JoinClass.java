package kz.iitu.diploma.project.model.filter;

import kz.iitu.diploma.project.model.*;
import lombok.Getter;

@Getter
public enum JoinClass {

    ROLE(Role.class),
    USER(User.class),
    USER_ROLE(UserRole.class),
    MESSAGE(Message.class),
    CHAT(Chat.class);

    private final Class<?> value;

    JoinClass(Class<?> value) {
        this.value = value;
    }

}
