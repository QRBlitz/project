package kz.iitu.diploma.project.model.dto;

import lombok.Builder;

@Builder
public class RoleDto {

    public Long id;
    public String code;
    public Boolean isActive;
    public String name;

}
