package com.hospital.controller;


import com.hospital.constants.AppConstants;

import com.hospital.dto.AppRequest;
import com.hospital.dto.PatientRequest;
import com.hospital.entity.*;
import com.hospital.repository.PatientRepository;
import com.hospital.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private AppointmentService appointmentService;

    Boolean closePopup = false;

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
    public String showAddPatientForm(Model model){
        PatientRequest patientrequest = new PatientRequest();
        model.addAttribute("patientRequest", patientrequest);
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
    public String addPatient(@Valid @ModelAttribute("patientRequest") PatientRequest patientRequest, BindingResult rBindingResult,
                             HttpSession session, RedirectAttributes redirectAttributes,Model model){
        System.out.println(patientRequest.getName());
        if (rBindingResult.hasErrors()) {
            return "addPatientForm";
        }
        //TODO: check the duplicate value
        Patient p = new Patient();
        p.setName(patientRequest.getName());
        p.setEmail(patientRequest.getEmail());
        p.setAge(patientRequest.getAge());
        p.setPhone(patientRequest.getPhone());
        p.setDob(patientRequest.getDob());
        p.setAadhar(patientRequest.getAadhar());
        p.setAddmissionDate(new Date());
        Patient createdPatient = null;

        try {
            Patient patient1 = patientService.findByAadhar(patientRequest.getAadhar());

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
            return "updatePatientForm";
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
            System.out.println("test");
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
        closePopup = true;
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
                try{
                    List<Date> availableDate = docSchedule.getAvailabledate();
                    Collections.sort(availableDate);
                    doc.setNextAvailableDate(availableDate.get(0));
                    doc.setPatientId(patient.getId());
                }catch(Exception e){

                }
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
                .sorted(Comparator.comparing(AppointmentDetails::getDateOfAppointment).reversed())
                .collect(Collectors.toList());
        Collections.reverse(sortedList);
        List<AppointmentDetails> latestFiveAppDetails = sortedList.stream().limit(6).collect(Collectors.toList());
        model.addAttribute("appDetails", latestFiveAppDetails);

        if(sortedList!=null && sortedList.size()>0){
            AppointmentDetails nextAppoinment = sortedList.get(0);
            model.addAttribute("nextAppoinment", nextAppoinment);
        }else{
            model.addAttribute("nextAppoinment", new AppointmentDetails());
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
            @RequestParam(value = "patientId", required = true) Long patientId,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,RedirectAttributes redirectAttributes, Model model) {

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
            doc.setPatientId(patientId);
            doctorList1.add(doc);
        }

        model.addAttribute("currentPage", pageNo+1);
        model.addAttribute("totalPages", doctorList.getTotalPages());
        model.addAttribute("totalItems", doctorList.getTotalElements());
        model.addAttribute("doctorList", doctorList1);


        return "linkdoctor";
    }


    @GetMapping("/linkdoctor/{patientId}/doctor/{doctorId}") //To link a specific patient with a specific doctor in a hospital
    public String linkPatientWithDoctor(@PathVariable Long patientId, @PathVariable Long doctorId,RedirectAttributes redirectAttributes, Model model){

        Patient patient = null;
        try {
            patient = patientService.getPatientByPatientId(patientId);
        }catch (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0";
        }

        Doctor doctor = null;
        try {
            doctor = doctorService.getDoctorById(doctorId);
        }catch (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0";
        }
        if(doctor!=null){
            doctor.setPatient(patient);
        }
        doctorService.saveDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "Doctor Linked Successfully.");
        redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");
        return "redirect:/patient/ui/linkdoctor/0?patientId="+patient.getId();
    }



    @GetMapping("/bookappointment/patient/{patientId}/doctor/{doctorId}/{bookdate}")
    public String bookPatientAppointment(
            @PathVariable Long patientId, @PathVariable Long doctorId,
            @PathVariable String bookdate, RedirectAttributes redirectAttributes, Model model
    ) throws ParseException {

        //get the patient details
        Patient patient = null;
        try {
            patient = patientService.getPatientByPatientId(patientId);
        }catch (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0?patientId="+patientId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0?patientId="+patientId;
        }

        //get the doctor details
        Doctor doctor = null;
        try {
            doctor = doctorService.getDoctorById(doctorId);
        }catch (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0?patientId="+patientId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            return "redirect:/patient/ui/linkdoctor/0?patientId="+patientId;
        }
        DoctorSchedule docSchedule = null;
        if(doctor!=null){
            docSchedule = doctor.getSchedule();
        }

        String sDate1 = bookdate;
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);

        //creating appointment details object
        AppointmentDetails appointmentDetails = new AppointmentDetails();
        appointmentDetails.setDateOfAppointment(date1);
        appointmentDetails.setAppointmentStatus(AppConstants.PENDING);

        Date appointmentDate = date1;
        Format f = new SimpleDateFormat("EEEE");        //logic to find day from a date
        String str = f.format(appointmentDate);

        appointmentDetails.setDocName(doctor.getName());
        appointmentDetails.setDayOfAppointment(str);
        appointmentDetails.setPatId(patient.getId());
        appointmentDetails.setDocId(doctor.getId());
        appointmentDetails.setPatient(patient);
        appointmentDetails.setCheckupRoom(docSchedule.getCheckuproom());
        AppointmentDetails app = appointmentService.saveAppoinmentDetails(appointmentDetails);

        List<Date> docAvailableDate = null;
        if(docSchedule != null){
            docAvailableDate =  docSchedule.getAvailabledate();   //all the date    1/2/3/4  //3
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String patAppointmentDate = formatter.format(date1);
        List<String> dateStringList = new ArrayList<>();
        for (Date date : docAvailableDate) {                    //this for loop is used to convert list of Date to List of String
            String dateStr = String.valueOf(date);              //Date -> String
            dateStringList.add(dateStr);
        }
        //yyyy-MM-dd
        if(dateStringList.contains(patAppointmentDate)){
            dateStringList.remove(patAppointmentDate);           //doctor new available date //String
        }

        docAvailableDate = new ArrayList<>();  //
        //String -> Date
        for(String strDate : dateStringList){
            DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter1.parse(strDate);
            docAvailableDate.add(date);   //doctor new available date //Date
        }
        docSchedule.setAvailabledate(docAvailableDate);
        doctor.setSchedule(docSchedule);
        Doctor d = doctorService.saveDoctor(doctor);

        //mail all the details to patient //krpi@gmail.com -> krpi@gmail.com
        String message = "Hi "+patient.getName()+","+"\n\nYour appointment has been scheduled successfully. Find the appointment details below."+
                "\n\nAppointment No: "+app.getId()+
                "\n\nDoctor Name: "+app.getDocName()+
                "\n\nAppointment Date: "+app.getDateOfAppointment()+
                "\n\nAppointment Day: "+app.getDayOfAppointment()+
                "\n\nCheckup Room: "+app.getCheckupRoom();

        try {
            if(patient.getEmail()==null){

            }else{
                emailService.sendEmail(patient.getEmail(), "Appointment Details",
                        message);
            }
            System.out.println("email sent");
        }catch (Exception e){
            System.out.println("email sent failed");
        }
        redirectAttributes.addFlashAttribute("message", "Appointment has been scheduled. Please check your email");
        redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");
        return "redirect:/patient/ui/linkdoctor/0?patientId="+patientId;
    }

    public Boolean getClosePopup() {
        return closePopup;
    }

    public void setClosePopup(Boolean closePopup) {
        this.closePopup = closePopup;
    }
}
