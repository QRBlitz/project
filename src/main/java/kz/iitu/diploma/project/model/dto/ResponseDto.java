package kz.iitu.diploma.project.model.dto;

import lombok.Builder;

@Builder
public class ResponseDto {

    public String message;
    public Long id;
    public Object dto;

    public ResponseDto(String message) {
        this.message = message;
    }

    public ResponseDto(Object dto) {
        this.dto = dto;
    }

    public ResponseDto(String message, Object dto) {
        this.message = message;
        this.dto = dto;
    }

    public ResponseDto(String message, Long id) {
        this.message = message;
        this.id = id;
    }

    public ResponseDto(String message, Long id, Object dto) {
        this.message = message;
        this.id = id;
        this.dto = dto;
    }

}
