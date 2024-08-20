package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import org.springframework.stereotype.Service;

@Service
public interface DoctorService {

    public Patient saveDoctor(Doctor doctor);  //C

}
