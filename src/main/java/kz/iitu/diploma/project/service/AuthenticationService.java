package kz.iitu.diploma.project.service;

import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<ResponseDto> authenticate(UserDto userDto);

    ResponseEntity<ResponseDto> register(UserDto userDto);

    ResponseEntity<ResponseDto> restorePassword(UserDto userDto);

}
