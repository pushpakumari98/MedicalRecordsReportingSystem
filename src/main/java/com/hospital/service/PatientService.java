package com.hospital.service;

import com.hospital.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService {

    //CRUD -

    public Patient savePatient(Patient patient);  //C

    public List<Patient> getAllPatient();   //R

    public Patient getPatientByPatientId(Long id);  //R

    public void deletePatientById(Long id);  //D

    public Patient findByAadhar(String aadhar);

    Page<Patient> getAllPatient(int page, int size, String sortBy, String direction);
}
