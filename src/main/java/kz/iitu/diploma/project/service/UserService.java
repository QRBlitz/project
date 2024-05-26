package kz.iitu.diploma.project.service;

import kz.iitu.diploma.project.model.filter.SearchRequest;
import kz.iitu.diploma.project.model.dto.DeleteDto;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ResponseDto> createUser(UserDto userDto);

    ResponseEntity<ResponseDto> searchUsers(SearchRequest request);

    ResponseEntity<ResponseDto> updateUser(UserDto userDto);

    ResponseEntity<ResponseDto> adminUpdateUser(UserDto userDto);

    ResponseEntity<ResponseDto> changePassword(UserDto userDto);

    ResponseEntity<ResponseDto> adminChangePassword(UserDto userDto);

    ResponseEntity<ResponseDto> blockUser(DeleteDto deleteDto);

    ResponseEntity<ResponseDto> unblockUser(Long userId);

    ResponseEntity<ResponseDto> giveRole(UserDto userDto);

    ResponseEntity<ResponseDto> removeRole(UserDto userDto);

}
