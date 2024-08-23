package com.hospital.service;

import com.hospital.entity.Doctor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DoctorService {



    public Doctor saveDoctor(Doctor doctor);  //C

    static List<Doctor> getAllDoctor() {
        return null;
    }

    Optional<Doctor> getDoctorById(Long doctorId);

    void deleteDoctorById(Long id);

    Doctor findById(Long id);

    Optional<Doctor> findByPhone(Long phone);

}
