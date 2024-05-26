package kz.iitu.diploma.project.service;

import kz.iitu.diploma.project.model.filter.SearchRequest;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.RoleDto;
import org.springframework.http.ResponseEntity;

public interface RoleService {

    ResponseEntity<ResponseDto> createRole(RoleDto roleDto);

    ResponseEntity<ResponseDto> updateRole(RoleDto roleDto);

    ResponseEntity<ResponseDto> deleteRole(String code);

    ResponseEntity<ResponseDto> searchRoles(SearchRequest request);

}