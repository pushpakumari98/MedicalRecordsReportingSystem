package com.hospital.controller;


import com.hospital.constants.AppConstants;

import com.hospital.dto.PatientRequest;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

import java.util.NoSuchElementException;


@Controller
@RequestMapping("/ui")
public class viewController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/home")
    public String hello(Model model){
        return "home";
    }

    @GetMapping("/patient")
    public String patientHome(){
        return "patientHome";
    }

    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model, @RequestParam(required = false, value="patientId") Long patientId){
        System.out.println("Patient Id "+patientId);
        PatientRequest patientrequest = new PatientRequest();
        if(patientId != null){
            Patient patient = patientRepository.findById(patientId).get();
            patientrequest.setName(patient.getName());
            patientrequest.setPhone(patient.getPhone());
            patientrequest.setDob(patient.getDob());
            patientrequest.setAadhar(patient.getAadhar());
            patientrequest.setAge(patient.getAge());
            patientrequest.setId(patient.getId());
        }

        model.addAttribute("patientrequest", patientrequest);
        return "addPatientForm";
    }

    @GetMapping("/updatePatientForm")
    public String updatePatientForm(Model model, @RequestParam(required = false, value="patientId") Long patientId){
        System.out.println("Patient Id "+patientId);
        PatientRequest patientrequest = new PatientRequest();
        if(patientId != null){
            Patient patient = patientRepository.findById(patientId).get();
            patientrequest.setName(patient.getName());
            patientrequest.setPhone(patient.getPhone());
            patientrequest.setDob(patient.getDob());
            patientrequest.setAadhar(patient.getAadhar());
            patientrequest.setAge(patient.getAge());
            patientrequest.setId(patient.getId());
        }

        model.addAttribute("patientrequest", patientrequest);
        return "updatePatientForm";
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

    @PostMapping("/updatepatient")
    public String updatePatient(@Valid @ModelAttribute("patientrequest") PatientRequest patientrequest, BindingResult rBindingResult,
                             HttpSession session, RedirectAttributes redirectAttributes){
        System.out.println(patientrequest.getName());
        if (rBindingResult.hasErrors()) {
            return "addPatientForm";
        }
        //TODO: check the duplicate value
        Patient p = new Patient();
        p.setId(patientrequest.getId());
        p.setName(patientrequest.getName());
        p.setAge(patientrequest.getAge());
        p.setPhone(patientrequest.getPhone());
        p.setDob(patientrequest.getDob());
        p.setAadhar(patientrequest.getAadhar());
        p.setAddmissionDate(new Date());
        Patient createdPatient = null;

        try {
            Patient patient1 = patientService.findByAadhar(patientrequest.getAadhar());

            patientService.savePatient(p);

        }catch  (NoSuchElementException e){
            createdPatient = patientService.savePatient(p);
            redirectAttributes.addFlashAttribute("message", "Patient has been saved successfully with patient id: "+createdPatient.getId());
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }
        return "redirect:/ui/updatePatientForm";
    }

    @GetMapping("/patientList/{pageNo}")
    public String viewPatientList(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,RedirectAttributes redirectAttributes) {

        Page<Patient> patientList = null;
        if(pageNo!=0){
            pageNo = pageNo -1;
        }
        try {
            patientList = patientService.getAllPatient(pageNo, size, sortBy, direction);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }

        model.addAttribute("currentPage", pageNo+1);
        model.addAttribute("totalPages", patientList.getTotalPages());
        model.addAttribute("totalItems", patientList.getTotalElements());
        model.addAttribute("patientList", patientList.getContent());

        //model.addAttribute("patientRequest", new PatientRequest());
        return "patientList";
    }
}
