package com.hospital.service;

import com.hospital.entity.Pharmacist;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PharmacistService {
    public  Pharmacist addPharmacist(Pharmacist pharmacist);
    public List<Pharmacist> getAllPharmacists();
    Pharmacist getPharmacistById(Long id);
    void deletePharmacist(Long id);

}
