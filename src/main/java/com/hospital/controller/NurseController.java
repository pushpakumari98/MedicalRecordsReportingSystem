package com.hospital.controller;
import com.hospital.dto.NurseRequest;
import com.hospital.entity.Doctor;
import com.hospital.entity.Nurse;
import com.hospital.service.NurseHistoryService;
import com.hospital.service.NurseService;
import com.hospital.serviceimpl.ExportExcel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
public class NurseController {
    @Autowired
    private NurseService nurseService;

    @Autowired
    private NurseHistoryService nurseHistoryService;

    @Autowired
    private ExportExcel exportExcel;

    @PostMapping("/nurse") //A new nurse will get created
    public ResponseEntity saveNurse(@Valid @RequestBody NurseRequest nurse, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(CREATED).body(errorList);
        }
        Nurse n = new Nurse();
        n.setName(nurse.getName());
        n.setDob(nurse.getDob());
        n.setAge(nurse.getAge().toString());
        n.setPhone(nurse.getPhone());

        Nurse createdNurse = null;

        try {
            Nurse nurse1 = nurseService.findByPhone(nurse.getPhone()).orElse(null);
            if (nurse1 == null) {
                createdNurse = nurseService.saveNurse(n);
            } else {
                return ResponseEntity.status(CREATED).body("Nurse already exists!! Your Nurse Id is: " + nurse1.getId());
            }
        } catch (NoSuchElementException e) {
            createdNurse = nurseService.saveNurse(n);
        } catch (Exception e) {
            return ResponseEntity.status(CREATED).body("Something went wrong. Please check with Admin!!");
        }
        return ResponseEntity.status(CREATED).body(createdNurse);
    }

    @GetMapping("/getnurse/{nurseId}")  //To retrieve information about a nurse based on specific ID
    public ResponseEntity getNurseById(@PathVariable Long nurseId) {

        System.out.println("nurseId " + nurseId);
        Nurse nurse = null;
        try {
            nurse = nurseService.getNurseById(nurseId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        return ResponseEntity.ok().body(nurse);
    }

    @GetMapping("/getNurse")  //To retrieve a list of all nurses in the hospital
    public ResponseEntity getAllNurse() {
        List<Nurse> NurseList = null;
        try {
            NurseList = nurseService.getAllNurse();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Something Went Wrong!");
        }
        return ResponseEntity.ok().body(NurseList);
    }

    @DeleteMapping("/deletenurse/{nurseId}")  //To delete details about a specific nurse using ID
    public ResponseEntity deleteNurseById(@RequestParam Long nurseId) {
        Nurse nurse = null;
        System.out.println("nurseId " + nurseId);
        try {
            try {
                Nurse nurse1 = nurseService.getNurseById(nurseId);

            } catch (NoSuchElementException e) {
                return ResponseEntity.status(NO_CONTENT).body("Nurse Does Not Exist!!!");
            }
            nurseService.deleteNurseById(nurseId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Something Went Wrong!");
        }
        return ResponseEntity.status(NO_CONTENT).body("Nurse has been deleted successfully");
    }

    @PutMapping("/updateNurse/{phone}") //To update the details of an existing nurse based on  phone number
    public ResponseEntity<?> updateNurse(@RequestBody NurseRequest nurseRequest, @PathVariable Long phone, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }

        try {
            Optional<?> NurseOpt = nurseService.findByPhone(phone);
            if (NurseOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Nurse does not exist! Please proceed with registration.");
            }
            Nurse dbNurse = (Nurse) NurseOpt.get();
            if (nurseRequest.getName() != null) {
                dbNurse.setName(nurseRequest.getName());
            }
            if (nurseRequest.getAge() != null) {
                dbNurse.setAge(nurseRequest.getAge().toString());
            }
            if (nurseRequest.getPhone() != null) {
                dbNurse.setPhone(nurseRequest.getPhone());
            }
            if (nurseRequest.getDob() != null) {
                dbNurse.setDob(nurseRequest.getDob());
            }
            Nurse updatedNurse = nurseService.saveNurse(dbNurse);
            return ResponseEntity.ok().body("Nurse Updated Successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong in the update.");
        }
    }

    @GetMapping("/export-nurse")
    public ResponseEntity exportNurse(HttpServletResponse response) throws IOException, IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Nurse_Info.xlsx";
        response.setHeader(headerKey, headerValue);
        exportExcel.exportNurseDataToExcel(response);
        return new ResponseEntity(HttpStatus.OK);
    }
}
