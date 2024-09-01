package com.hospital.controller;
import com.hospital.dto.DoctorRequest;
import com.hospital.dto.DoctorScheduleRequest;
import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorSchedule;
import com.hospital.repository.DoctorRepository;
import com.hospital.service.DoctorService;
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

@RestController
@RequestMapping("/api")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    DoctorRepository doctorRepository;
    @PostMapping("/doctor") //A new doctor will get created
    public ResponseEntity<?> saveDoctor(@Valid @RequestBody DoctorRequest doctor, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }

        Doctor d = new Doctor();
        d.setName(doctor.getName());          // Ensure that getName() method exists
        d.setDob(doctor.getDob());            // Ensure that getDob() method exists
        d.setAge(doctor.getAge());            // Ensure that getAge() method exists
        d.setPhone(doctor.getPhone());        // Ensure that getPhone() method exists
        d.setSpecialist(doctor.getSpecialist()); // Ensure that getSpecialist() method exists

        Doctor createdDoctor = null;
        try {
            Doctor doctor1 = doctorService.findByPhone(doctor.getPhone()).orElse(null);
            if (doctor1 == null) {
                createdDoctor = doctorService.saveDoctor(d);  //
            } else {
                return ResponseEntity.badRequest().body("Patient already exists!! Your Patient Id is: " + doctor1.getId());
            }
        } catch (NoSuchElementException e) {
            createdDoctor = doctorService.saveDoctor(d);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong. Please check with Admin!!");
        }
        return ResponseEntity.ok().body(createdDoctor);

    }
    @GetMapping("/getDoctor")  //To retrieve a list of all doctors in the hospital
    public ResponseEntity getAllDoctor () {
        List<Doctor> DoctorList = null;
        try {
            DoctorList = doctorService.getAllDoctor();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }
        return ResponseEntity.ok().body(DoctorList);
    }
    @GetMapping("/getdoctor/{doctorId}") //To retrieve information about a nurse based on specific ID
    public ResponseEntity getDoctorById (@PathVariable Long doctorId){

        System.out.println("doctorId " + doctorId);
        Doctor doctor = null;
        try {
            doctor = doctorService.getDoctorById(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        return ResponseEntity.ok().body(doctor);
    }

    @DeleteMapping("/deletedoctor/{doctorId}")  //To delete details about a specific doctor using ID
    public ResponseEntity deleteDoctorById (@RequestParam Long doctorId){
        Doctor doctor = null;
        System.out.println("doctorId " + doctorId);
        try {
            try {
                Doctor doctor1 = doctorService.getDoctorById(doctorId);

            } catch (NoSuchElementException e) {
                return ResponseEntity.ok().body("Doctor Does Not Exist!!!");
            }
            doctorService.deleteDoctorById(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }
        return ResponseEntity.ok().body("Doctor has been deleted successfully");
    }

    @PutMapping("/updateDoctor/{phone}") //To update the details of an existing doctor based on phone number
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorRequest doctorRequest, @PathVariable Long phone, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }

        try {
            Optional<Doctor> dbDoctorOpt = doctorService.findByPhone(phone);
            if (dbDoctorOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Doctor does not exist! Please proceed with registration.");
            }

            Doctor dbDoctor = dbDoctorOpt.get();

            if (doctorRequest.getName() != null) {
                dbDoctor.setName(doctorRequest.getName());
            }
            if (doctorRequest.getAge() != null) {
                dbDoctor.setAge(doctorRequest.getAge());
            }
            if (doctorRequest.getPhone() != null) {
                dbDoctor.setPhone(doctorRequest.getPhone());
            }
            if (doctorRequest.getDob() != null) {
                dbDoctor.setDob(doctorRequest.getDob());
            }
            if (doctorRequest.getSpecialist() != null) {
                dbDoctor.setSpecialist(doctorRequest.getSpecialist());
            }
            Doctor updatedDoctor = doctorService.saveDoctor(dbDoctor);
            return ResponseEntity.ok().body("Doctor Updated Successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong in the update.");
        }
    }

    @GetMapping("/findBySpe/{specialist}")  //To retrieve a list of doctors who specialize in a specific disease
    public ResponseEntity findBySpecialist(@PathVariable String specialist){
        System.out.println(specialist);
        List<Doctor> docList = doctorService.findBySpecialist(specialist);
        return ResponseEntity.ok().body(docList);
    }

    @PostMapping("/createdocschedule/{docid}")  //To create a schedule for a specific doctor based on the provided details
    public ResponseEntity findBySpecialist(@RequestBody DoctorScheduleRequest doctorScheduleRequest, @PathVariable Long docid){

        System.out.println(doctorScheduleRequest);
        System.out.println(docid);
        boolean validDays = validateDays(doctorScheduleRequest.getWorkingdays());
        if(!validDays){
            return ResponseEntity.ok().body("Please correct the days format! follow the given date format:-" +
                    "MON,TUE,WED,THU,FRI,SAT,SUN");
        }


        //TODO: Validate that request has the corret days
        Doctor doctor = null;
        System.out.println("doctorId " + docid);
        try {
            try {
                doctor = doctorService.getDoctorById(docid);

            } catch (NoSuchElementException e) {
                return ResponseEntity.ok().body("Doctor Does Not Exist!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setWorkingdays(doctorScheduleRequest.getWorkingdays());
        schedule.setCheckuproom(doctorScheduleRequest.getCheckuproom());

        doctor.setSchedule(schedule);

        Doctor doc = doctorService.saveDoctor(doctor);

        return ResponseEntity.ok().body(doc);
    }

    private boolean validateDays(List<String> workingdays) {
        boolean valid = true;

        for(String day:workingdays){
            if(day.equalsIgnoreCase("MON"))
            {

            }
            else if(day.equalsIgnoreCase("TUE"))
            {

            }
            else if(day.equalsIgnoreCase("WED"))
            {

            }
            else if(day.equalsIgnoreCase("THU"))
            {

            }
            else if(day.equalsIgnoreCase("FRI"))
            {

            }
            else if(day.equalsIgnoreCase("SAT"))
            {

            }
            else if(day.equalsIgnoreCase("SUN"))
            {

            }
            else{
                valid = false;
            }
        }

        return valid;

    }

}
