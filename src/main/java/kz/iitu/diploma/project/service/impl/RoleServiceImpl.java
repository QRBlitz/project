package kz.iitu.diploma.project.service.impl;

import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.model.Role;
import kz.iitu.diploma.project.model.filter.SearchRequest;
import kz.iitu.diploma.project.model.filter.SearchSpecification;
import kz.iitu.diploma.project.service.RoleService;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.RoleDto;
import kz.iitu.diploma.project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<ResponseDto> createRole(RoleDto roleDto) {
        try {
            Role role = new Role();
            role.setCode(roleDto.code);
            role.setName(roleDto.name);
            role.setIsActive(true);
            role = roleRepository.save(role);

            return ResponseEntity.ok(new ResponseDto("Role created successfully", role));
        } catch (Exception e) {
            log.error("service: createRole, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updateRole(RoleDto roleDto) {
        try {
            Role role = roleRepository.findByCode(roleDto.code);
            if (role == null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.ROLE_NOT_FOUND));
            }
            if (roleDto.name != null) role.setName(roleDto.name);
            if (roleDto.isActive != null) role.setIsActive(roleDto.isActive);

            return ResponseEntity.ok(new ResponseDto("Role updated successfully", role));
        } catch (Exception e) {
            log.error("service: updateRole, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> deleteRole(String code) {
        try {
            Role role = roleRepository.findByCode(code);
            if (role == null) {
                return ResponseEntity.badRequest().body(new ResponseDto(CustomException.ROLE_NOT_FOUND));
            }
            roleRepository.delete(role);

            return ResponseEntity.ok(new ResponseDto("Role deleted successfully"));
        } catch (Exception e) {
            log.error("service: deleteRole, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> searchRoles(SearchRequest request) {
        try {
            SearchSpecification<Role> specification = new SearchSpecification<>(request);
            Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
            return ResponseEntity.ok(new ResponseDto(roleRepository.findAll(specification, pageable)));
        } catch (Exception e) {
            log.error("service: searchRoles, exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.SOMETHING_WENT_WRONG));
        }
    }

}