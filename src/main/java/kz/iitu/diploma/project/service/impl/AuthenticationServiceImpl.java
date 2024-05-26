package kz.iitu.diploma.project.service.impl;

import kz.iitu.diploma.project.component.JwtTokenUtils;
import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.model.Chat;
import kz.iitu.diploma.project.model.UserRole;
import kz.iitu.diploma.project.service.AuthenticationService;
import kz.iitu.diploma.project.model.User;
import kz.iitu.diploma.project.model.dto.EmailDto;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import kz.iitu.diploma.project.repository.ChatRepository;
import kz.iitu.diploma.project.repository.RoleRepository;
import kz.iitu.diploma.project.repository.UserRepository;
import kz.iitu.diploma.project.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ChatRepository chatRepository;
    private final EmailServiceImpl emailService;
    private final MessageSource messageSource;

    private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public ResponseEntity<ResponseDto> authenticate(UserDto userDto) {
        try {
            if (userDto.login == null || userDto.login.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.WRONG_LOGIN_OR_PASSWORD));
            }
            User user = userRepository.findByLogin(userDto.login);
            if (user == null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND));
            }
            if (!passwordEncoder.matches(userDto.password, user.getPassword())) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.WRONG_LOGIN_OR_PASSWORD));
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.login, userDto.password));
            userRepository.updateLastLoginTime(userDto.login);
            String jwtToken = jwtTokenUtils.generateToken(new HashMap<>(), user);

            return ResponseEntity.ok(new ResponseDto("Successfully authorized", user.getId(), jwtToken));
        } catch (Exception e) {
            log.error("service: authenticate, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> register(UserDto userDto) {
        try {
            if (userRepository.findByLogin(userDto.login) != null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_ALREADY_EXIST));
            }
            String password = passwordEncoder.encode(userDto.password);

            User user = userDto.toCreateEntity(password);
            user = userRepository.save(user);
            user.setCreatedBy(user);
            user = userRepository.save(user);

            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleRepository.findByCode("USER").getId());
            userRoleRepository.save(userRole);

            Chat chat = Chat.builder()
                    .senderId(user.getId())
                    .build();
            chatRepository.save(chat);

            return ResponseEntity.ok(new ResponseDto("Successfully registered", user));
        } catch (Exception e) {
            log.error("service: register, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> restorePassword(UserDto userDto) {
        try {
            if (!userRepository.existsByLoginAndBlockedIsFalse(userDto.login))
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND));
            User user = userRepository.findByLogin(userDto.login);
            String email = user.getEmail();
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body(new ResponseDto("В Вашей учетной записи отсутствует почта, обратитесь в службу поддержки"));
            }

            String newPassword = generateRandomPassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            user = userRepository.save(user);

            EmailDto emailDto = EmailDto.builder()
                    .subject("Новый пароль")
                    .text(messageSource.getMessage("password.restore", null, Locale.US).replace("login_text", user.getLogin()).replace("password_text", newPassword))
                    .to(email)
                    .build();
            emailService.sendMimeMessage(emailDto);

            return ResponseEntity.ok(new ResponseDto("Password changed successfully"));
        } catch (Exception e) {
            log.error("service: restorePassword, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    private String generateRandomPassword() {
        int passwordLength = 12;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
