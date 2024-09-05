package com.hospital.controller;


import com.hospital.constants.AppConstants;
import com.hospital.dto.DoctorRequest;
import com.hospital.dto.PatientRequest;
import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui")
public class helloController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/home")
    public String hello(Model model){
        return "home";
    }

    @GetMapping("/patient")
    public String patientHome(){
        return "patientHome";
    }

    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model){
        PatientRequest patientrequest = new PatientRequest();
        model.addAttribute("patientrequest", patientrequest);
        return "addPatientForm";
    }

    @PostMapping("/savepatient")
    public String addPatient(@Valid @ModelAttribute("patientrequest") PatientRequest patientrequest, BindingResult rBindingResult,
                             HttpSession session, RedirectAttributes redirectAttributes){
        System.out.println(patientrequest.getName());
        if (rBindingResult.hasErrors()) {
            return "addPatientForm";
        }
        //TODO: check the duplicate value
        Patient p = new Patient();
        p.setName(patientrequest.getName());
        p.setAge(patientrequest.getAge());
        p.setPhone(patientrequest.getPhone());
        p.setDob(patientrequest.getDob());
        p.setAadhar(patientrequest.getAadhar());
        p.setAddmissionDate(new Date());
        Patient createdPatient = null;

        try {
            Patient patient1 = patientService.findByAadhar(patientrequest.getAadhar());

            if (patient1 == null) {
                createdPatient = patientService.savePatient(p);   //
                redirectAttributes.addFlashAttribute("message", "Patient has been saved successfully with patient id: "+createdPatient.getId());
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Patient already exist!! Your Patient Id is: " + patient1.getId());
                redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
             }
        }catch  (NoSuchElementException e){
            createdPatient = patientService.savePatient(p);
            redirectAttributes.addFlashAttribute("message", "Patient has been saved successfully with patient id: "+createdPatient.getId());
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }
        return "redirect:/ui/addPatientForm";
    }

    @GetMapping("/patientList")
    public String viewPatientList(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
                                          @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                          @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,RedirectAttributes redirectAttributes) {

        Page<Patient> patientList = null;
        try {
            patientList = patientService.getAllPatient(page, size, sortBy, direction);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }

        model.addAttribute("patientList", patientList);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("patientRequest", new PatientRequest());
        return "patientList";
    }
}
