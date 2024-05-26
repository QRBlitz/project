package kz.iitu.diploma.project.service.impl;

import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.service.VerificationCodeService;
import kz.iitu.diploma.project.component.HelperClass;
import kz.iitu.diploma.project.model.VerificationCode;
import kz.iitu.diploma.project.model.dto.EmailDto;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import kz.iitu.diploma.project.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final HelperClass helperClass;
    private final EmailServiceImpl emailService;

    @Value("${is.test}")
    private String isTest;

    @Value("${site.link}")
    private String siteLink;

    @Override
    public ResponseEntity<ResponseDto> sendVerificationCodeEmail(UserDto userDto) {
        try {
            if (verificationCodeRepository.checkIsAlreadySentByEmail(userDto.email) != null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.CODE_ALREADY_SENT));
            }
            VerificationCode verificationCode = VerificationCode.builder()
                    .code(helperClass.createRandomNumber())
                    .email(userDto.email)
                    .build();
            verificationCodeRepository.save(verificationCode);

            String body = "Confirmation code: " + verificationCode.getCode();
            EmailDto emailDto = EmailDto.builder()
                    .to(verificationCode.getEmail())
                    .text(body)
                    .subject("Confirm your email address")
                    .build();
            emailService.sendMimeMessage(emailDto);

            return ResponseEntity.ok(new ResponseDto("Verification code has been sent"));
        } catch (Exception e) {
            log.error("service: sendVerificationCodeEmail, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

}
