package com.hospital.service;

import com.hospital.dto.PatientDetailsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientDetailsService {

    public List<PatientDetailsResponse> getPatientDetails();

}
