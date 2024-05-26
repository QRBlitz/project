package kz.iitu.diploma.project.model.dto;

import kz.iitu.diploma.project.model.User;
import lombok.Builder;

import java.util.List;

@Builder
public class UserDto {

    public Long id;
    public String firstName;
    public String lastName;
    public String login;
    public String password;
    public String phone;
    public String email;
    public String avatar;
    public String sex;
    public Boolean isBlocked;
    public List<RoleDto> roles;
    public String verificationCode;
    public String oldPassword;
    public String newPassword;
    public String hashCode;

    public User toCreateEntity(String password) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .login(login)
                .password(password)
                .phone(phone)
                .email(email)
                .sex(sex)
                .avatar(avatar)
                .build();
    }

}
