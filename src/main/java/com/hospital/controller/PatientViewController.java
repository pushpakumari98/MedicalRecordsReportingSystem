package com.hospital.controller;


import com.hospital.constants.AppConstants;

import com.hospital.dto.AppRequest;
import com.hospital.dto.PatientRequest;
import com.hospital.entity.*;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AddressService;
import com.hospital.service.DoctorService;
import com.hospital.service.EmailService;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/patient/ui")
public class PatientViewController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DoctorService doctorService;

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
        return "redirect:/patient/ui/addPatientForm";
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
        Patient createdPatient = null;

        try {
            Patient patient1 = patientService.findByAadhar(patientrequest.getAadhar());
            if(patient1==null){
                redirectAttributes.addFlashAttribute("message", "No Such Patient exist with aadhar :"+patientrequest.getAadhar());
                redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
                return "redirect:/patient/ui/updatePatientForm";
            }
            p.setAddmissionDate(patient1.getAddmissionDate());
            patientService.savePatient(p);
            redirectAttributes.addFlashAttribute("message", "Patient has been updated successfully");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }catch  (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", "No Such Patient exist with aadhar :"+patientrequest.getAadhar());
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }
        return "redirect:/patient/ui/updatePatientForm";
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


        return "redirect:/patient/ui/patientList/0";
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

        return "redirect:/patient/ui/mailform?patientId="+composeForm.getPatientId();
    }

    @GetMapping("/viewpatientdetails")
    public String viewPatientDetails(@RequestParam(required = false, value="patientId") Long patientId
            , RedirectAttributes redirectAttributes, Model model){

        Patient patient = patientRepository.findById(patientId).get();
        model.addAttribute("patient", patient);


        Address address =  patient.getAddress();
        model.addAttribute("address", address);
        List<Doctor> docList = patient.getDoctor();
        List<Doctor> doctorList = new ArrayList<>();
        if(docList!=null && docList.size()>0){
            for(Doctor doc : docList){
                DoctorSchedule docSchedule = doc.getSchedule();
                List<Date> availableDate = docSchedule.getAvailabledate();
                Collections.sort(availableDate);
                doc.setNextAvailableDate(availableDate.get(0));
                doctorList.add(doc);
            }
        }

        List<Doctor> docSortedList = doctorList.stream()
                .sorted(Comparator.comparing(Doctor::getId).reversed())
                .collect(Collectors.toList());
        List<Doctor> latestFiveDoctorDetails = docSortedList.stream().limit(5).collect(Collectors.toList());
        model.addAttribute("doctorList", doctorList);

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


        List<AppointmentDetails> appDetails = patient.getAppDetails();
        List<AppointmentDetails> sortedList = appDetails.stream()
                .sorted(Comparator.comparing(AppointmentDetails::getId).reversed())
                .collect(Collectors.toList());
        List<AppointmentDetails> latestFiveAppDetails = sortedList.stream().limit(10).collect(Collectors.toList());
        model.addAttribute("appDetails", latestFiveAppDetails);


        List<AppointmentDetails> sortedList1 = appDetails.stream()
                .sorted(Comparator.comparing(AppointmentDetails::getDateOfAppointment).reversed())
                .collect(Collectors.toList());
        if(sortedList1!=null && sortedList1.size()>0){
            AppointmentDetails nextAppoinment = sortedList1.get(0);
            model.addAttribute("nextAppoinment", nextAppoinment);
        }





        model.addAttribute("docSchedule", new DoctorSchedule());


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

        return "redirect:/patient/ui/viewpatientdetails?patientId="+addressForm.getPatientId();
    }

    @GetMapping("/linkdoctor/{pageNo}")
    public String linkPatientDoctorList(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,RedirectAttributes redirectAttributes) {

        Page<Doctor> doctorList = null;
        if(pageNo!=0){
            pageNo = pageNo -1;
        }
        try {
            doctorList = doctorService.getAllDoctor(pageNo, size, sortBy, direction);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }

        List<Doctor> tmpDoctorList = doctorList.getContent();
        List<Doctor> doctorList1 = new ArrayList<>();

        for(Doctor doc : tmpDoctorList){
            if(doc.getSchedule() !=null){
                doc.setAvailableDate(doc.getSchedule().getAvailabledate());
            }
            doctorList1.add(doc);
        }

        model.addAttribute("currentPage", pageNo+1);
        model.addAttribute("totalPages", doctorList.getTotalPages());
        model.addAttribute("totalItems", doctorList.getTotalElements());
        model.addAttribute("doctorList", doctorList1);

        DoctorSchedule test = doctorList1.get(0).getSchedule();

        model.addAttribute("test", test);

        return "linkdoctor";
    }

}
