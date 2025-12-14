package com.example.kitchen.eventListener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.example.kitchen.service.email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final EmailService emailService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            log.info("ApplicationStartupListener.onApplicationEvent");
            emailService.sendStartupMail();
        } catch (Exception e) {
            log.error("Error in onApplicationEvent ", e);
        }
    }
}
