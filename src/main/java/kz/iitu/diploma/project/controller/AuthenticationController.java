package kz.iitu.diploma.project.controller;

import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import kz.iitu.diploma.project.service.AuthenticationService;
import kz.iitu.diploma.project.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDto> authenticate(@RequestBody UserDto userDto) {
        return authenticationService.authenticate(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody UserDto userDto) {
        return authenticationService.register(userDto);
    }

    @PostMapping("/restorePassword")
    public ResponseEntity<ResponseDto> restorePassword(@RequestBody UserDto userDto) {
        return authenticationService.restorePassword(userDto);
    }

}
