package kz.iitu.diploma.project.service.impl;

import kz.iitu.diploma.project.component.JwtTokenUtils;
import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.model.Chat;
import kz.iitu.diploma.project.model.Role;
import kz.iitu.diploma.project.model.User;
import kz.iitu.diploma.project.model.UserRole;
import kz.iitu.diploma.project.model.dto.*;
import kz.iitu.diploma.project.model.filter.SearchRequest;
import kz.iitu.diploma.project.model.filter.SearchSpecification;
import kz.iitu.diploma.project.repository.ChatRepository;
import kz.iitu.diploma.project.repository.RoleRepository;
import kz.iitu.diploma.project.repository.UserRepository;
import kz.iitu.diploma.project.repository.UserRoleRepository;
import kz.iitu.diploma.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;
    private final MessageSource messageSource;
    private final ChatRepository chatRepository;

    @Override
    public ResponseEntity<ResponseDto> createUser(UserDto userDto) {
        try {
            User userExist = userRepository.findByLogin(userDto.login);
            if (userExist != null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_ALREADY_EXIST));
            }
            String password = passwordEncoder.encode(userDto.password);

            User user = userDto.toCreateEntity(password);
            user = userRepository.save(user);
            if (userDto.roles != null && !userDto.roles.isEmpty()) {
                for (RoleDto role : userDto.roles) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    userRole.setUserId(roleRepository.findByCode(role.code).getId());
                    userRoleRepository.save(userRole);
                }
            } else {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleRepository.findByCode("USER").getId());
                userRoleRepository.save(userRole);
            }

            Chat chat = Chat.builder()
                    .senderId(user.getId())
                    .build();
            chatRepository.save(chat);

            return ResponseEntity.ok(new ResponseDto("User successfully created", user));
        } catch (Exception e) {
            log.error("service: createUser, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> searchUsers(SearchRequest request) {
        try {
            SearchSpecification<User> specification = new SearchSpecification<>(request);
            Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
            return ResponseEntity.ok(new ResponseDto(userRepository.findAll(specification, pageable)));
        } catch (Exception e) {
            log.error("service: searchUsers, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updateUser(UserDto userDto) {
        try {
            User user = userRepository.findByLogin(jwtTokenUtils.getCurrentUserName());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND));
            }
            update(user, userDto, false);
            user = userRepository.save(user);

            return ResponseEntity.ok(new ResponseDto("User updated successfully", user));
        } catch (Exception e) {
            log.error("service: updateProfile, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> adminUpdateUser(UserDto userDto) {
        try {
            Optional<User> userExist = userRepository.findById(userDto.id);
            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND));
            }
            User user = userExist.get();
            update(user, userDto, true);
            user = userRepository.save(user);

            return ResponseEntity.ok(new ResponseDto("User updated successfully", user));
        } catch (Exception e) {
            log.error("service: adminUpdateUser, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> changePassword(UserDto userDto) {
        try {
            User user = userRepository.findByLogin(jwtTokenUtils.getCurrentUserName());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND));
            }
            if (!passwordEncoder.matches(userDto.oldPassword, user.getPassword())) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.OLD_PASSWORD_DOESNT_MATCH));
            }
            if (userDto.oldPassword.equals(userDto.newPassword)) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.OLD_NEW_PASSWORD_MATCH));
            }
            user.setPassword(passwordEncoder.encode(userDto.newPassword));
            userRepository.save(user);
            jwtTokenUtils.logout();

            return ResponseEntity.ok(new ResponseDto("Password changed successfully"));
        } catch (Exception e) {
            log.error("service: changePassword, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> adminChangePassword(UserDto userDto) {
        try {
            Optional<User> userExist = userRepository.findById(userDto.id);
            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND));
            }
            User user = userExist.get();
            user.setPassword(passwordEncoder.encode(userDto.newPassword));
            userRepository.save(user);

            return ResponseEntity.ok(new ResponseDto("Password changed successfully"));
        } catch (Exception e) {
            log.error("service: adminChangePassword, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> blockUser(DeleteDto deleteDto) {
        try {
            Optional<User> userExist = userRepository.findById(deleteDto.id);
            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND, deleteDto.id));
            }
            if (jwtTokenUtils.getCurrentUserName().equals(userExist.get().getUsername())) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.CAN_NOT_BLOCK_YOURSELF));
            }
            User user = userExist.get();
            if (!user.getBlocked()) {
                userRepository.blockUser(deleteDto.id);
                if (user.getEmail() != null) {
                    EmailDto emailDto = EmailDto.builder()
                            .to(user.getEmail())
                            .text(messageSource.getMessage("block.user.message", null, Locale.US).replace("reason_text", deleteDto.reason))
                            .subject("Your account has been blocked")
                            .build();
                    emailService.sendMimeMessage(emailDto);
                }
                return ResponseEntity.ok(new ResponseDto("User successfully blocked"));
            }

            return ResponseEntity.ok(new ResponseDto("The user is already blocked"));
        } catch (Exception e) {
            log.error("service: blockUser, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> unblockUser(Long userId) {
        try {
            Optional<User> userExist = userRepository.findById(userId);
            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND, userId));
            }
            User user = userExist.get();
            if (user.getBlocked()) {
                userRepository.unblockUser(userId);
                return ResponseEntity.ok(new ResponseDto("User successfully unlocked"));
            }

            return ResponseEntity.ok(new ResponseDto("The user is not blocked"));
        } catch (Exception e) {
            log.error("service: unblockUser, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> giveRole(UserDto userDto) {
        try {
            Optional<User> userExist = userRepository.findById(userDto.id);
            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND, userDto.id));
            }
            User user = userExist.get();
            for (RoleDto role : userDto.roles) {
                if (!user.getRoles().contains(roleRepository.findByCode(role.code))) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleRepository.findByCode(role.code).getId());
                    userRoleRepository.save(userRole);
                }
            }

            return ResponseEntity.ok(new ResponseDto("Role added to user successfully"));
        } catch (Exception e) {
            log.error("service: giveRole, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> removeRole(UserDto userDto) {
        try {
            Optional<User> userExist = userRepository.findById(userDto.id);
            if (userExist.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND, userDto.id));
            }
            User user = userExist.get();
            for (RoleDto it : userDto.roles) {
                Role role = roleRepository.findByCode(it.code);
                UserRole userRole = userRoleRepository.findByUserIdAndRoleId(user.getId(), role.getId());
                if (userRole != null) {
                    userRoleRepository.delete(userRole);
                }
            }

            return ResponseEntity.ok(new ResponseDto("The user's role has been successfully removed"));
        } catch (Exception e) {
            log.error("service: removeRole, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    private void update(User user, UserDto userDto, Boolean admin) {
        if (userDto.firstName != null) user.setFirstName(userDto.firstName);
        if (userDto.lastName != null) user.setLastName(userDto.lastName);
        if (userDto.phone != null) user.setPhone(userDto.phone);
        if (userDto.email != null) user.setEmail(userDto.email);
        if (userDto.avatar != null) user.setAvatar(userDto.avatar);
        if (userDto.sex != null) user.setSex(userDto.sex);
        if (userDto.login != null && admin) user.setLogin(userDto.login);
    }


}
