package com.hospital.serviceimpl;
import com.hospital.entity.Doctor;
import com.hospital.repository.DoctorRepository;
import com.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    @Override
    public void deleteDoctorById(Long id) {
        doctorRepository.deleteById(id); //deleteById  -> void
    }

    @Override
    public Doctor findById(Long id) {
        return null;
    }

    @Override
    public Optional<Doctor> findByPhone(Long phone) {
        return doctorRepository.findByPhone(phone);
    }

}