package com.hospital.controller;
import com.hospital.dto.AddressRequest;
import com.hospital.entity.Address;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    PatientRepository patientRepository;

    @PostMapping("/savePatientAddress/{patientid}")  //To save or update the address of a specific patient
    public ResponseEntity savePatientAddress(@Valid @RequestBody AddressRequest addressRequest, @PathVariable Long patientid) {

        //check if the patient is exist in the hospital or not
        //if not, return a valid message else proceed to save address
        Patient patient = null;
        try {
            patient = patientService.getPatientByPatientId(patientid);
            if (patient == null) {
                return ResponseEntity.status(CREATED).body("Patient Does not exist!");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(CREATED).body("Patient Does not exist!");
        }

        if (patient != null) {

            Address dbAddress = patient.getAddress();
            Address add = new Address();
            if (dbAddress != null) {
                add.setId(dbAddress.getId());
            }
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());
            add.setAddType("Patient");

            patient.setAddress(add);

            patientService.savePatient(patient);
        }

        return ResponseEntity.status(CREATED).body("Address saved successfully!!");
    }

    @PostMapping("/createdocaddress/{doctorid}") // To save or update the address of a specific doctor
    public ResponseEntity saveDoctorAddress(@Valid @RequestBody AddressRequest addressRequest, @PathVariable Long doctorid) {

        //check if the patient is exist in the hospital or not
        //if not, return a valid message else proceed to save address
        Doctor doctor = null;
        try {
            doctor = doctorService.findById(doctorid);
            if (doctor == null) {
                return ResponseEntity.status(CREATED).body("Doctor Does not exist!");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(CREATED).body("doctor Does not exist!");
        }

        if (doctor != null) {

            Address add = new Address();
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());
            add.setAddType("Doctor");

            doctor.setAddress(add);

            doctorService.saveDoctor(doctor);
        }

        return ResponseEntity.status(CREATED).body("doctor address saved successfully!!");
    }

    @PutMapping("/updatePatientAddress/{patientid}")  //To update the address of a specific patient
    public ResponseEntity updatePatientAddress(@RequestBody AddressRequest addressRequest, @PathVariable Long patientid) {

        //check if the patient is exist in the hospital or not
        //if not, return a valid message else proceed to save address
        Patient patient = null;
        try {
            patient = patientService.getPatientByPatientId(patientid);
            if (patient == null) {
                return ResponseEntity.ok().body("Patient Does not exist!");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Patient Does not exist!");
        }

        Address add = new Address();

        Address address = patient.getAddress();
        if (address == null) {
            //createing new address
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());
            add.setAddType("Patient");
            patient.setAddress(add);
        } else if (address != null) {
            //updating exiting address
            add.setId(address.getId());
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());
            add.setAddType("Patient");
            patient.setAddress(add);
        }
        patientService.savePatient(patient);
        return ResponseEntity.ok().body("Address Updated successfully!!");
    }

    @PutMapping("/updateDoctorAddress/{doctorid}")  //To update the address of a specific doctor
    public ResponseEntity updateDoctorAddress(@RequestBody AddressRequest addressRequest, @PathVariable Long doctorid) {

        try {
            Doctor doctor = null;
            doctor = doctorService.getDoctorById(doctorid);
            if (doctor == null) {
                return ResponseEntity.ok().body("Doctor Does not exist!");
            }
            Address address = doctor.getAddress();

            // If the address exists, update it
            if (address != null) {
                address.setId(address.getId());
                address.setState(addressRequest.getState());
                address.setCountry(addressRequest.getCountry());
                address.setCity(addressRequest.getCity());
                address.setPin(addressRequest.getPin());
                address.setAddType("Doctor");
            } else {
                // Otherwise, create a new address
                address = new Address();
                address.setState(addressRequest.getState());
                address.setCountry(addressRequest.getCountry());
                address.setCity(addressRequest.getCity());
                address.setPin(addressRequest.getPin());
                address.setAddType("Doctor");
            }
            // Associate the address with the doctor and save
            doctor.setAddress(address);
            doctorService.saveDoctor(doctor);

            return ResponseEntity.ok().body("Doctor's address updated successfully!");

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Doctor does not exist!");
        }
    }
}
