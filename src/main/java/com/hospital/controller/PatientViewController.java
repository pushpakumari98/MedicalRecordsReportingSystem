package com.hospital.controller;


import com.hospital.constants.AppConstants;

import com.hospital.dto.PatientRequest;
import com.hospital.entity.*;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AddressService;
import com.hospital.service.EmailService;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/ui")
public class PatientViewController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AddressService addressService;

    @GetMapping("/home")
    public String hello(Model model){
        return "home";
    }

    @GetMapping("/patient")
    public String patientHome(){
        return "patientHome";
    }

    @Autowired
    public EmailService emailService;

    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model){
        PatientRequest patientrequest = new PatientRequest();
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
            patientrequest.setEmail(patient.getEmail());
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
        p.setEmail(patientrequest.getEmail());
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
        p.setEmail(patientrequest.getEmail());
        p.setAge(patientrequest.getAge());
        p.setPhone(patientrequest.getPhone());
        p.setDob(patientrequest.getDob());
        p.setAadhar(patientrequest.getAadhar());
        p.setAddmissionDate(new Date());
        Patient createdPatient = null;

        try {
            Patient patient1 = patientService.findByAadhar(patientrequest.getAadhar());

            patientService.savePatient(p);
            redirectAttributes.addFlashAttribute("message", "Patient has been updated successfully");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }catch  (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", "Something went wrong.");
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

    @GetMapping("/deletePatient")
    public String deletePatient(@RequestParam(required = false, value="patientId") Long patientId
            , RedirectAttributes redirectAttributes){

        System.out.println(patientId);

        try{
            patientService.deletePatientById(patientId);
            redirectAttributes.addFlashAttribute("message", "Patient Deleted Succesfully");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("message", "Exception in Patient deletion");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }


        return "redirect:/ui/patientList/0";
    }

    @GetMapping("/mailform")
    public String composeMailForm(@RequestParam(required = false, value="patientId") Long patientId
            , RedirectAttributes redirectAttributes, Model model) {

        System.out.println(patientId);
        Patient patient = patientRepository.findById(patientId).get();

        ComposeMailForm composeForm = new ComposeMailForm();
        composeForm.setEmail(patient.getEmail().toString());
        composeForm.setPatientId(patient.getId());
        model.addAttribute("composeForm", composeForm);
        return "composemail";
    }

    @RequestMapping(value = "/sendmail", method = RequestMethod.POST)
    public String sendmail(@Valid @ModelAttribute ComposeMailForm composeForm,BindingResult bindingResult, HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("Hello");
        try {
            emailService.sendEmail(composeForm.getEmail(), composeForm.getSubject(),
                    composeForm.getMessage());
            redirectAttributes.addFlashAttribute("message", "Mail sent");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Mail send failed");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }

        return "redirect:/ui/mailform?patientId="+composeForm.getPatientId();
    }

    @GetMapping("/viewpatientdetails")
    public String viewPatientDetails(@RequestParam(required = false, value="patientId") Long patientId
            , RedirectAttributes redirectAttributes, Model model){

        Patient patient = patientRepository.findById(patientId).get();
        Address address =  patient.getAddress();
        Doctor doctor = patient.getDoctor();
        if(doctor==null){
            doctor = new Doctor();
            doctor.setName("Piyush Kumar");
        }

        DoctorSchedule docSchedule = doctor.getSchedule();
        List<Date> availableDate = docSchedule.getAvailabledate();
        Collections.sort(availableDate);
        docSchedule.setNextAvailableDate(availableDate.get(0));
        System.out.println(patientId);
        AddressForm addressForm = new AddressForm();
        if(address==null){
            addressForm.setPatientId(patient.getId());
        }else{
            addressForm.setAddId(address.getId());
            addressForm.setAddressId(address.getId());
            addressForm.setPatientId(patient.getId());
            addressForm.setAddressId(address.getId());
            addressForm.setPin(address.getPin());
            addressForm.setCity(address.getCity());
            addressForm.setCountry(address.getCountry());
            addressForm.setAddType(address.getAddType());
            addressForm.setState(address.getState());
        }

        model.addAttribute("addressForm", addressForm);
        model.addAttribute("patient", patient);
        model.addAttribute("address", address);
        model.addAttribute("doctor", doctor);
        model.addAttribute("docSchedule", docSchedule);
        return "patientdetails";
    }

    @PostMapping("/saveaddressform")
    public String saveAddressForm(
            @Valid @ModelAttribute AddressForm addressForm,RedirectAttributes redirectAttributes, Model model) {

        System.out.println(addressForm.getPatientId());
        Patient patient = patientRepository.findById(addressForm.getPatientId()).get();
        Address address = patient.getAddress();
        if(address==null){
            Address newAddress = new Address();
            newAddress.setCity(addressForm.getCity());
            newAddress.setPin(addressForm.getPin());
            newAddress.setCountry(addressForm.getCountry());
            newAddress.setState(addressForm.getState());
            newAddress.setAddType(addressForm.getAddType());
            patient.setAddress(newAddress);
        }else{
            address.setId(addressForm.getAddressId());
            address.setCity(addressForm.getCity());
            address.setPin(addressForm.getPin());
            address.setCountry(addressForm.getCountry());
            address.setState(addressForm.getState());
            address.setAddType(addressForm.getAddType());
            patient.setAddress(address);
        }
        try {
            Patient savedPatient = addressService.savePatient(patient);
            redirectAttributes.addFlashAttribute("message", "Address has been saved successfully.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Address Save failed.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }

        return "redirect:/ui/viewpatientdetails?patientId="+addressForm.getPatientId();
    }


}
