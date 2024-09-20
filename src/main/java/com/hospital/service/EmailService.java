package com.hospital.service;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface EmailService {
    void sendEmail(String to, String subject, String body) throws UnsupportedEncodingException;//
}
