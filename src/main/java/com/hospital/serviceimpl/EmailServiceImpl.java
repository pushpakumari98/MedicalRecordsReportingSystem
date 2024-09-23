package com.hospital.serviceimpl;

import com.hospital.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender eMailSender;

    @Override
    public void sendEmail(String to, String subject, String body) throws UnsupportedEncodingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(String.valueOf(new InternetAddress("pushpa20052002kumari@gmail.com", "Pushpa Kumari")));
        eMailSender.send(message);
    }
}
