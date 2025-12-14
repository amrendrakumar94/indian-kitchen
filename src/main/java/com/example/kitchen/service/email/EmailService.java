package com.example.kitchen.service.email;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendStartupMail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("amrendraadvik786@gmail.com");
            message.setSubject("Spring Boot Server Started");
            message.setText("Indian Kitchen Server started successfully on EC2 at " + java.time.LocalDateTime.now());
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Sending Email Failed", e);
        }
    }
}
