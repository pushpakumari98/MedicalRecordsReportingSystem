package com.hospital.controller;

import com.hospital.dto.AddressRequest;
import com.hospital.entity.Address;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientRepository patientRepository;


    @PostMapping("/createaddress/{patientid}")
    public ResponseEntity saveAddress(@Valid @RequestBody AddressRequest addressRequest, @PathVariable Long patientid){

        //check if the patient is exist in the hospital or not
        //if not, return a valid message else procedd to save address
        Patient patient = null;
        try {
            patient = patientService.getPatientByPatientId(patientid);
            if(patient==null){
                return ResponseEntity.ok().body("Patient Does not exist!");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Patient Does not exist!");
        }

        if (patient != null) {

            Address dbAddress = patient.getAddress();
            Address add = new Address();
            if(dbAddress !=null){
                add.setId(dbAddress.getId());
            }
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());
            add.setAddType("Patient");

            patient.setAddress(add); ///

            patientService.savePatient(patient);  //
        }

        return ResponseEntity.ok().body("Address saved sucessfully!!");
    }

    @PostMapping("/createdocaddress/{doctorid}")
    public ResponseEntity saveDoctorAddress(@Valid @RequestBody AddressRequest addressRequest, @PathVariable Long doctorid){

        //check if the patient is exist in the hospital or not
        //if not, return a valid message else procedd to save address
        Doctor doctor = null;
        try {
            doctor = doctorService.findById(doctorid);
            if(doctor==null){
                return ResponseEntity.ok().body("Doctor Does not exist!");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("doctor Does not exist!");
        }

        if (doctor != null) {

            Address add = new Address();
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());
            add.setAddType("Doctor");

            doctor.setAddress(add); ///

            doctorService.saveDoctor(doctor);  //
        }

        return ResponseEntity.ok().body("doctor saved sucessfully!!");
    }

}
