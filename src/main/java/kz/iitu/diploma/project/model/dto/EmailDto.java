package kz.iitu.diploma.project.model.dto;

import lombok.Builder;

@Builder
public class EmailDto {

    public String to;
    public String from;
    public String subject;
    public String text;

}
