package com.hospital.service;

import com.hospital.entity.Nurse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NurseService {
    public Nurse saveNurse(Nurse nurse);
    public List<Nurse> getAllNurse();
    Nurse getNurseById(Long nurseId);
    void deleteNurseById(Long id);
    Nurse findById(Long id);
    Optional<Nurse> findByPhone(Long phone);
}
