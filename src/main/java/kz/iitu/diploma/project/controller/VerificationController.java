package kz.iitu.diploma.project.controller;

import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import kz.iitu.diploma.project.service.VerificationCodeService;
import kz.iitu.diploma.project.service.impl.VerificationCodeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/verification")
public class VerificationController {

    private final VerificationCodeServiceImpl verificationCodeService;

    @PostMapping("/sendEmail")
    public ResponseEntity<ResponseDto> sendEmail(@RequestBody UserDto userDto) {
        return verificationCodeService.sendVerificationCodeEmail(userDto);
    }

}
