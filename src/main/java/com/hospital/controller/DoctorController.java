package com.hospital.controller;

import com.hospital.dto.DoctorReq;
import com.hospital.entity.Doctor;
import com.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorservice;
    @PostMapping("Doctor")
    public <DoctorRequest> ResponseEntity saveDoctor(@Valid @RequestBody DoctorReq doctor , BindingResult result){
      if(result.hasErrors()){
          List<String> errorList = result.getAllErrors().stream()
                  .map(DefaultMessageSourceResolvable::getDefaultMessage)
                  .collect(Collectors.toList());
          return ResponseEntity.badRequest().body(errorList);
      }
      Doctor d=new Doctor();
      d.setName(doctor.getName());
      d.setDob(doctor.getDob());
      d.setId(doctor.getId());
      d.setAge(doctor.getAge());
      d.setPhone(doctor.getPhone());
      d.setCity(doctor.getCity());
      d.setState(doctor.getState());
      d.setCountry(doctor.getCountry());
      d.setSpecialist(doctor.getSpecialist());

        return null;
    }
}
