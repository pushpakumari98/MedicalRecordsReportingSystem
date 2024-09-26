package com.hospital.serviceimpl;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.DoctorRepository;
import com.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Doctor saveDoctor(Doctor doctor) {

        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctor() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).get();
    }
    @Override
    public void deleteDoctorById(Long id) {
        doctorRepository.deleteById(id); //deleteById  -> void
    }

    @Override
    public Doctor findById(Long id) {
        return doctorRepository.findById(id).get();
    }

    @Override
    public Optional<Doctor> findByPhone(Long phone) {
        return doctorRepository.findByPhone(phone);
    }

    @Override
    public List<Doctor> findBySpecialist(String specialist) {
        return doctorRepository.findBySpecialist(specialist);
    }

    @Override
    public Page<Doctor> getAllDoctor(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return doctorRepository.findAll(pageable);

    }

}