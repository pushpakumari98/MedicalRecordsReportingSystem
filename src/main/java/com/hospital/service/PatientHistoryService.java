package com.hospital.service;

import com.hospital.entity.PatientHistory;
import org.springframework.stereotype.Service;

@Service
public interface PatientHistoryService {

    public PatientHistory saveDeletedPaitent(PatientHistory patient);

}
