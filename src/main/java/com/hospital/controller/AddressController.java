package com.hospital.controller;

import com.hospital.dto.AddressRequest;
import com.hospital.entity.Address;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
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

            Address add = new Address();
            add.setState(addressRequest.getState());
            add.setCountry(addressRequest.getCountry());
            add.setCity(addressRequest.getCity());
            add.setPin(addressRequest.getPin());

            patient.setAddress(add); ///

            patientService.savePatient(patient);  //
        }

        return null;
    }


}
