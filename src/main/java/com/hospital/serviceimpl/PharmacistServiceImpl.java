package com.hospital.serviceimpl;

import com.hospital.entity.Pharmacist;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PharmacistRepository;
import com.hospital.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacistServiceImpl implements PharmacistService {

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Override
    public Pharmacist addPharmacist(Pharmacist pharmacist) {
        return pharmacistRepository.save(pharmacist);
    }

    @Override
    public List<Pharmacist> getAllPharmacists() {
        return pharmacistRepository.findAll();
    }

    @Override
    public Pharmacist getPharmacistById(Long id) {
        return pharmacistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacist not found"));
    }

    @Override
    public void deletePharmacist(Long id) {
        pharmacistRepository.deleteById(id);
    }
}

