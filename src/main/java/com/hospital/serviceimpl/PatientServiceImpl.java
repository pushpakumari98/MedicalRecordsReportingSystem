package com.hospital.serviceimpl;

import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    //IOC
    //DI
    //ui -> controller -> service -> repo -> db
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient savePatient(Patient patient) {

        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatient() {

        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientByPatientId(Long id) {
          return patientRepository.findById(id).get();
    }

    @Override
    public void deletePatientById(Long id) {

        patientRepository.deleteById(id); //deleteById  -> void
    }

    @Override
    public Patient findByAadhar(String aadhar) {

        return patientRepository.findByAadhar(aadhar).get();  //Optional se body extract k liye get()
    }

    @Override
    public Page<Patient> getAllPatient(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return patientRepository.findAll(pageable);
    }
}
