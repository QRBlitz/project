package kz.iitu.diploma.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.model.User;
import kz.iitu.diploma.project.model.dto.DeleteDto;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import kz.iitu.diploma.project.model.filter.SearchRequest;
import kz.iitu.diploma.project.repository.UserRepository;
import kz.iitu.diploma.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(new ResponseDto(value))).orElseGet(() -> ResponseEntity.badRequest().body(new ResponseDto(CustomException.USER_NOT_FOUND, id)));
    }

    @PostMapping("/createUser")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PostMapping("/searchUsers")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> searchUsers(@RequestBody SearchRequest request) {
        return userService.searchUsers(request);
    }

    @PatchMapping("/updateUser")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @PatchMapping("/adminUpdateUser")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> adminUpdateUser(@RequestBody UserDto userDto) {
        return userService.adminUpdateUser(userDto);
    }

    @PatchMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody UserDto userDto) {
        return userService.changePassword(userDto);
    }

    @PatchMapping("/adminChangePassword")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> adminChangePassword(@RequestBody UserDto userDto) {
        return userService.adminChangePassword(userDto);
    }

    @PatchMapping("/blockUser")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> blockUser(@RequestBody DeleteDto deleteDto) {
        return userService.blockUser(deleteDto);
    }

    @PatchMapping("/unblockUser/{userId}")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> unblockUser(@PathVariable Long userId) {
        return userService.unblockUser(userId);
    }

    @PatchMapping("/giveRole")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> giveRole(@RequestBody UserDto userDto) {
        return userService.giveRole(userDto);
    }

    @PatchMapping("/removeRole")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> removeRole(@RequestBody UserDto userDto) {
        return userService.removeRole(userDto);
    }

    @GetMapping("/test")
    public String password() {
        return generateRandomPassword();
    }

    private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    private String generateRandomPassword() {
        int passwordLength = 16;
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
