package com.hospital.controller;

import com.hospital.dto.PatientRequest;
import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class PatientController {
    @Autowired
    private PatientService patientService;
    //localhost:8081/api/patient

    //POST //GET//DELETE//PUT
    @PostMapping("/patient") //data create create ho rha h
    public ResponseEntity savePatient(@Valid @RequestBody PatientRequest patient, BindingResult result)
    {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }
            //TODO: check the duplicate value
        Patient p = new Patient();
        p.setName(patient.getName());
        p.setAge(patient.getAge());
        p.setPhone(patient.getPhone());
        p.setDob(patient.getDob());
        p.setCity(patient.getCity());
        p.setState(patient.getState());
        p.setCountry(patient.getCountry());
        p.setAadhar(patient.getAadhar());
        Patient createdPatient = null;

        try {
            Patient patient1 = patientService.findByAadhar(patient.getAadhar());

            if(patient1 == null){
                createdPatient = patientService.savePatient(p);
            }else{
                return ResponseEntity.badRequest().body("Patient already exist!! Your Patient Id is: "+patient1.getId());
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Something went wrong. Please check with Admin!!");
        }
        return ResponseEntity.ok().body(createdPatient);
    }

    @GetMapping("/getpatient")
    public ResponseEntity getAllPatient(){
        List<Patient> patientList = null;
        try{
            patientList =  patientService.getAllPatient();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        return ResponseEntity.ok().body(patientList);
    }

    @GetMapping("/getpatient/{patientId}")
    public ResponseEntity getPatientById(@RequestParam Long patientId){

        System.out.println("patientId "+patientId);
        Patient patient = null;
        try{
            patient =  patientService.getPatientByPatientId(patientId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        return ResponseEntity.ok().body(patient);
    }

    @DeleteMapping("/deletepatient/{patientId}")
    public ResponseEntity deletePatientById(@RequestParam Long patientId){
        Patient patient = null;
        System.out.println("patientId "+patientId);
        try{

            try{
                patientService.getPatientByPatientId(patientId);

            }catch(NoSuchElementException e){
                return ResponseEntity.ok().body("Patient Does Not Exist!!!");
            }
            patientService.deletePatientById(patientId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        return ResponseEntity.ok().body("Patient has been deleted successfully");
    }

    //@PutMapping("/updatePatient") //to update any data  we use @Putmapping
    @RequestMapping(value = "/updatePatient/{aadhar}", method = RequestMethod.PUT)
    public ResponseEntity updatePatient(@RequestBody PatientRequest patient, String aadhar, BindingResult result){

//        if (result.hasErrors()) {
//            List<String> errorList = result.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errorList);
//        }

        try{
            Patient dbPatient = patientService.findByAadhar(aadhar);
            if(dbPatient == null){
                return ResponseEntity.badRequest().body("Patient does not exist!!Please Procced for registration.");
            }else{
                //patient
                //dbPatient null
                // null != null
                // TRUE &&  !false
                if(patient.getName()!=null && !patient.getName().equalsIgnoreCase(dbPatient.getName())){
                    dbPatient.setName(patient.getName());
                }

                if(patient.getAadhar()!=null && !patient.getAadhar().equalsIgnoreCase(dbPatient.getAadhar())){
                    dbPatient.setAadhar(patient.getAadhar());
                }
                //
                //

                try{
                    Patient updatedPatient = patientService.savePatient(dbPatient);
                }catch(Exception e){
                    return ResponseEntity.badRequest().body("Something went wrong in update.");
                }
            }
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong in update.");
        }

        return ResponseEntity.badRequest().body("Patient Update Succesfully.");
    }


}
