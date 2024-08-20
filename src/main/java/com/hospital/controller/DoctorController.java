package com.hospital.controller;

import com.hospital.dto.DoctorReq;
import com.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorservice;
    @PostMapping("Doctor")
    public <DoctorRequest> ResponseEntity saveDoctor(@Valid @RequestBody DoctorReq doctor , BindingResult result){
      if(result.hasErrors()){

      }
        return null;
    }
}
