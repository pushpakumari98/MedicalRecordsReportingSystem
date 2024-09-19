package com.hospital.controller;

import com.hospital.entity.Pharmacist;
import com.hospital.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PharmacistController {

    @Autowired
    private PharmacistService pharmacistService;

    @PostMapping("/pharmacist")
    public ResponseEntity<Pharmacist> createPharmacist(@RequestBody Pharmacist pharmacist) {
        Pharmacist newPharmacist = pharmacistService.addPharmacist(pharmacist);
        return new ResponseEntity<>(newPharmacist, HttpStatus.CREATED);
    }

    @GetMapping("/pharmacists")
    public List<Pharmacist> getAllPharmacists() {
        return pharmacistService.getAllPharmacists();
    }

    @GetMapping("/pharmacist/{id}")
    public ResponseEntity<Pharmacist> getPharmacistById(@PathVariable Long id) {
        Pharmacist pharmacist = pharmacistService.getPharmacistById(id);
        return new ResponseEntity<>(pharmacist, HttpStatus.OK);
    }

    @DeleteMapping("/pharmacist/{id}")
    public ResponseEntity<Void> deletePharmacist(@PathVariable Long id) {
        pharmacistService.deletePharmacist(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
