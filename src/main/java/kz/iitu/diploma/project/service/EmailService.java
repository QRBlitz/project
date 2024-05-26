package kz.iitu.diploma.project.service;

import kz.iitu.diploma.project.model.dto.EmailDto;

public interface EmailService {

    void sendMimeMessage(EmailDto emailDto);

}
