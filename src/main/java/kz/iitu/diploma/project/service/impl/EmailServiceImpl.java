package kz.iitu.diploma.project.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kz.iitu.diploma.project.service.EmailService;
import kz.iitu.diploma.project.model.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Override
    public void sendMimeMessage(EmailDto emailDto) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(mailUsername);
            helper.setTo(emailDto.to);
            helper.setSubject(emailDto.subject);
            helper.setText(emailDto.text);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("service: sendMimeMessage, exception: {}", e.getMessage());
        }
    }

}
