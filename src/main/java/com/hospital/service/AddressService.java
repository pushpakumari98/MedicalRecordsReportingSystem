package com.hospital.service;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AddressService {
    public Patient savePatient(Patient patient);  //C

    public List<Patient> getAllPatient();   //R

    public Patient getPatientByPatientId(Long id);  //R

    public void deletePatientById(Long id);  //D

    public Patient findByAadhar(String aadhar);

    //public Address findAddressById

    public Doctor saveDoctor(Doctor doctor);  //C

    public List<Doctor> getAllDoctor();


    public Doctor getDoctorById(Long doctorId);

    void deleteDoctorById(Long id);

    Optional<Doctor> findByPhone(Long phone);
}
