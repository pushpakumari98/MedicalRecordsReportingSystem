
package com.hospital.serviceimpl;

import com.hospital.entity.Nurse;
import com.hospital.repository.NurseRepository;
import com.hospital.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private NurseRepository nurseRepository;
    @Override
    public Nurse saveNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    @Override
    public List<Nurse> getAllNurse() {
        return nurseRepository.findAll();
    }

    @Override
    public Nurse getNurseById(Long nurseId) {
        return nurseRepository.findById(nurseId).get();
    }

    @Override
    public void deleteNurseById(Long id) {
        nurseRepository.deleteById(id);
    }

    @Override
    public Nurse findById(Long id) {
        return nurseRepository.findById(id).get();
    }

    @Override
    public Optional<Nurse> findByPhone(Long phone) {
        return nurseRepository.findByPhone(phone);
    }
}
