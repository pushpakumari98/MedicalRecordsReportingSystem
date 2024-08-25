package com.hospital.controller;
import com.hospital.dto.DoctorReq;
import com.hospital.entity.Doctor;
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

    @PostMapping("/doctor")
    public <DoctorRequest> ResponseEntity saveDoctor(@Valid @RequestBody DoctorReq doctor, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }
        Doctor d = new Doctor();
        d.setName(doctor.getName());
        d.setDob(doctor.getDob());
        d.setAge(doctor.getAge());
        d.setPhone(doctor.getPhone());
        d.setCity(doctor.getCity());
        d.setState(doctor.getState());
        d.setCountry(doctor.getCountry());
        d.setSpecialist(doctor.getSpecialist());

        Doctor createdDoctor = null;
        try {
            DoctorRepository doctorService = null;
            Optional<Doctor> doctor1 = doctorService.findByPhone(doctor.getPhone());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong. Please check with Admin!!");
        }
        return ResponseEntity.ok().body(createdDoctor);
    }
    @GetMapping("/getDoctor")
    public ResponseEntity getAllDoctor () {
        List<Doctor> DoctorList = null;
        try {
            DoctorList = DoctorService.getAllDoctor();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }
        return ResponseEntity.ok().body(DoctorList);
    }
    @GetMapping("/getdoctor/{doctorId}")
    public ResponseEntity getDoctorById (@PathVariable Long doctorId){

        System.out.println("doctorId " + doctorId);
        Optional<Doctor> doctor = null;
        try {
            doctor = doctorService.getDoctorById(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        return ResponseEntity.ok().body(doctor);
    }

    @DeleteMapping("/deletedoctor/{doctorId}")
    public ResponseEntity deleteDoctorById (@RequestParam Long doctorId){
        Doctor doctor = null;
        System.out.println("doctorId " + doctorId);
        try {

            DoctorController doctorService = null;
            try {
                doctorService.getDoctorByDoctorId(doctorId);

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

    private void getDoctorByDoctorId(Long doctorId) {
    }
    @PutMapping("/updateDoctor/{phone}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorReq doctorReq, @PathVariable Long phone, BindingResult result) {
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

            if (doctorReq.getName() != null) {
                dbDoctor.setName(doctorReq.getName());
            }
            if (doctorReq.getAge() != null) {
                dbDoctor.setAge(doctorReq.getAge());
            }
            if (doctorReq.getPhone() != null) {
                dbDoctor.setPhone(doctorReq.getPhone());
            }
            if (doctorReq.getDob() != null) {
                dbDoctor.setDob(doctorReq.getDob());
            }
            if (doctorReq.getCity() != null) {
                dbDoctor.setCity(doctorReq.getCity());
            }
            if (doctorReq.getState() != null) {
                dbDoctor.setState(doctorReq.getState());
            }
            if (doctorReq.getCountry() != null) {
                dbDoctor.setCountry(doctorReq.getCountry());
            }
            if (doctorReq.getSpecialist() != null) {
                dbDoctor.setSpecialist(doctorReq.getSpecialist());
            }
            Doctor updatedDoctor = doctorService.saveDoctor(dbDoctor);
            return ResponseEntity.ok().body("Doctor Updated Successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong in the update.");
        }
    }
}
