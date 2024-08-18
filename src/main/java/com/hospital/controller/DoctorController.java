package com.hospital.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorservice;
    @PostMapping("Doctor")
    public <DoctorRequest> ResponseEntity saveDoctor(@Valid @RequestBody DoctorRequest doctor , BindingResult result){
      if(result.hasErrors()){

      }
        return null;
    }
}
