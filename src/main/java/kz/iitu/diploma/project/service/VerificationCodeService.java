package kz.iitu.diploma.project.service;

import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface VerificationCodeService {

    ResponseEntity<ResponseDto> sendVerificationCodeEmail(UserDto userDto);

}
