package kz.iitu.diploma.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.model.Role;
import kz.iitu.diploma.project.model.dto.ResponseDto;
import kz.iitu.diploma.project.model.dto.RoleDto;
import kz.iitu.diploma.project.model.filter.SearchRequest;
import kz.iitu.diploma.project.repository.RoleRepository;
import kz.iitu.diploma.project.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    private final RoleRepository roleRepository;
    private final RoleService roleService;

    @GetMapping("/{code}")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> getRole(@PathVariable String code) {
        Role role = roleRepository.findByCode(code);
        if (role == null) {
            return ResponseEntity.badRequest().body(new ResponseDto(CustomException.ROLE_NOT_FOUND));
        }
        return ResponseEntity.ok(new ResponseDto(role));
    }

    @GetMapping("/getRoleNames")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> getRoleNames() {
        return ResponseEntity.ok(new ResponseDto(roleRepository.getRoleNames()));
    }

    @GetMapping("/getRoles")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> getRoles() {
        return ResponseEntity.ok(new ResponseDto(roleRepository.findAll()));
    }

    @PostMapping("/searchRoles")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> searchRoles(@RequestBody SearchRequest request) {
        return roleService.searchRoles(request);
    }

    @PostMapping("/createRole")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> createRole(@RequestBody RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }

    @PatchMapping("/updateRole")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> updateRole(@RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }

    @DeleteMapping("/deleteRole/{code}")
    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDto> deleteRole(@PathVariable String code) {
        return roleService.deleteRole(code);
    }

}
