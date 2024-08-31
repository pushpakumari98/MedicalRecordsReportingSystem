package com.hospital.serviceimpl;

import com.hospital.entity.PatientHistory;
import com.hospital.repository.PatientHistoryRepository;
import com.hospital.service.PatientHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientHistoryServiceImpl implements PatientHistoryService {

    @Autowired
    PatientHistoryRepository patientHistoryRepository;

    @Override
    public PatientHistory saveDeletedPatient(PatientHistory patient) {
       return patientHistoryRepository.save(patient) ;
    }
}
