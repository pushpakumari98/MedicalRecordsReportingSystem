package com.hospital.serviceimpl;

import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AddressServiceImpl implements AddressService {

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
    public Doctor saveDoctor(Doctor doctor) {
        return null;
    }

    @Override
    public List<Doctor> getAllDoctor() {
        return null;
    }

    @Override
    public Doctor getDoctorById(Long doctorId) {

        return null;
    }

    @Override
    public void deleteDoctorById(Long id) {

    }

    @Override
    public Optional<Doctor> findByPhone(Long phone) {
        return Optional.empty();
    }
}
