package com.hospital.service;

import com.hospital.entity.Doctor;

import com.hospital.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DoctorService {
    public Doctor saveDoctor(Doctor doctor);  //C

    public List<Doctor> getAllDoctor();

    Doctor getDoctorById(Long doctorId);

    void deleteDoctorById(Long id);

    Doctor findById(Long id);

    Optional<Doctor> findByPhone(Long phone);

    List<Doctor> findBySpecialist(String specialist);

    Page<Doctor> getAllDoctor(int page, int size, String sortBy, String direction);

}
