package com.hospital.controller;

import com.hospital.dto.PharmacistRequest;
import com.hospital.entity.Nurse;
import com.hospital.entity.Pharmacist;
import com.hospital.service.PharmacistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
public class PharmacistController {

    @Autowired
    private PharmacistService pharmacistService;

    @PostMapping("/Pharmacist") // A new pharmacist will get created
    public ResponseEntity<?> savePharmacist(@Valid @RequestBody PharmacistRequest pharmacist, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(BAD_REQUEST).body(errorList);
        }

        Pharmacist ph = new Pharmacist();
        ph.setName(pharmacist.getName());
        ph.setDob(pharmacist.getDob());
        ph.setAge(pharmacist.getAge());
        ph.setPhone(pharmacist.getPhone());
        ph.setSpecialization(pharmacist.getSpecialization());

        Pharmacist createdPharmacist = null;

        try {
            Pharmacist pharmacist1 = pharmacistService.findByPhone(pharmacist.getPhone());
            if (pharmacist1 == null) {
                createdPharmacist = pharmacistService.savePharmacist(ph);
            } else {
                return ResponseEntity.status(CREATED).body("Nurse already exists!! Your Nurse Id is: " + pharmacist1.getId());
            }
        } catch (NoSuchElementException e) {
            createdPharmacist = pharmacistService.savePharmacist(ph);
        } catch (Exception e) {
            return ResponseEntity.status(CREATED).body("Something went wrong. Please check with Admin!!");
        }
        return ResponseEntity.status(CREATED).body(createdPharmacist);
    }

    @GetMapping("/getPharmacist/{pharmacistId}")  // To retrieve information about a pharmacist based on specific ID
    public ResponseEntity<?> getPharmacistById(@PathVariable Long pharmacistId) {
        try {
            Pharmacist pharmacist = pharmacistService.getPharmacistById(pharmacistId);
            return ResponseEntity.ok(pharmacist);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(NOT_FOUND).body("Pharmacist not found.");
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }
    }

    @GetMapping("/getPharmacist")  // To retrieve a list of all pharmacists in the hospital
    public ResponseEntity<?> getAllPharmacists() {
        try {
            List<Pharmacist> pharmacistList = pharmacistService.getAllPharmacists();
            return ResponseEntity.ok(pharmacistList);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }
    }

    @DeleteMapping("/deletePharmacist/{pharmacistId}")  // To delete details about a specific pharmacist using ID
    public ResponseEntity<?> deletePharmacistById(@PathVariable Long pharmacistId) {
        try {
            Pharmacist pharmacist = pharmacistService.getPharmacistById(pharmacistId);
            if (pharmacist == null) {
                return ResponseEntity.status(NOT_FOUND).body("Pharmacist does not exist!");
            }
            pharmacistService.deletePharmacistById(pharmacistId);
            return ResponseEntity.status(NO_CONTENT).body("Pharmacist has been deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(NOT_FOUND).body("Pharmacist does not exist!");
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }
    }

    @PutMapping("/updatePharmacist/{phone}") // To update the details of an existing Pharmacist based on phone number
    public ResponseEntity<?> updatePharmacist(@Valid @RequestBody PharmacistRequest pharmacistRequest, @PathVariable String phone, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }

        try {
            Optional<Pharmacist> PharmacistOpt = Optional.ofNullable(pharmacistService.findByPhone(phone));
            if (PharmacistOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Pharmacist does not exist! Please proceed with registration.");
            }

            Pharmacist dbPharmacist = PharmacistOpt.get();
            if (pharmacistRequest.getName() != null) {
                dbPharmacist.setName(pharmacistRequest.getName());
            }
            if (pharmacistRequest.getAge() != null) {
                dbPharmacist.setAge(pharmacistRequest.getAge());
            }
            if (pharmacistRequest.getPhone() != null) {
                dbPharmacist.setPhone(pharmacistRequest.getPhone());
            }
            if (pharmacistRequest.getDob() != null) {
                dbPharmacist.setDob(pharmacistRequest.getDob());
            }
            if (pharmacistRequest.getSpecialization() != null) {
                dbPharmacist.setSpecialization(pharmacistRequest.getSpecialization());
            }

            Pharmacist updatedPharmacist = pharmacistService.savePharmacist(dbPharmacist);
            return ResponseEntity.ok("Pharmacist updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Something went wrong during the update.");
        }
    }
}
