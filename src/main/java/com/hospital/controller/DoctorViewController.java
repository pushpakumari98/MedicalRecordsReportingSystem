package com.hospital.controller;

import com.hospital.constants.AppConstants;
import com.hospital.dto.DoctorRequest;
import com.hospital.dto.DoctorScheduleRequest;
import com.hospital.entity.Doctor;
import com.hospital.service.DoctorService;
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

import java.util.NoSuchElementException;

@RequestMapping("/doctor/ui")
@Controller
public class DoctorViewController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @GetMapping("/home")
    public String hello(Model model){
        return "home";
    }

    @GetMapping("/doctor")
    public String patientHome(){
        return "/doctor/doctorhome";
    }


    @GetMapping("/addDoctorForm")
    public String addDoctorForm(Model model){
        DoctorRequest doctorRequest = new DoctorRequest();
        model.addAttribute("doctorRequest", doctorRequest);
        return "/doctor/adddoctorform";
    }

    @PostMapping("/savedoctor")
    public String addDoctor(@Valid @ModelAttribute("doctorRequest") DoctorRequest doctorRequest, BindingResult rBindingResult,
                             HttpSession session, RedirectAttributes redirectAttributes){
        System.out.println(doctorRequest.getName());
        if (rBindingResult.hasErrors()) {
            return "/doctor/adddoctorform";
        }
        //TODO: check the duplicate value
        Doctor doctorentity = new Doctor();
        doctorentity.setName(doctorRequest.getName());
        doctorentity.setEmail(doctorRequest.getEmail());
        doctorentity.setAge(doctorRequest.getAge());
        doctorentity.setPhone(doctorRequest.getPhone());
        doctorentity.setDob(doctorRequest.getDob());
        doctorentity.setSpecialist(doctorRequest.getSpecialist());
        Doctor createdPatient = null;

        try {
            Doctor doctor = doctorService.findByPhone(doctorRequest.getPhone()).get();

            if (doctor == null) {
                createdPatient = doctorService.saveDoctor(doctorentity);   //
                redirectAttributes.addFlashAttribute("message", "Patient has been saved successfully with patient id: "+createdPatient.getId());
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Doctor already exist!! Your Doctor Id is: " + doctor.getId());
                redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            }
        }catch  (NoSuchElementException e){
            createdPatient = doctorService.saveDoctor(doctorentity);   //
            redirectAttributes.addFlashAttribute("message", "Patient has been saved successfully with patient id: "+createdPatient.getId());
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }
        return "redirect:/doctor/ui/adddoctorform";
    }


    @GetMapping("/doctorList/{pageNo}")
    public String viewDoctorList(
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

        model.addAttribute("currentPage", pageNo+1);
        model.addAttribute("totalPages", doctorList.getTotalPages());
        model.addAttribute("totalItems", doctorList.getTotalElements());
        model.addAttribute("doctorList", doctorList.getContent());

        //model.addAttribute("patientRequest", new PatientRequest());
        return "/doctor/doctorlist";
    }


    @GetMapping("/scheduleForm")
    public String addDoctorSchedule(Model model,
                                    @RequestParam(value = "doctorId", required = true) Long doctorId){
        System.out.println("doctorId: "+doctorId);
        DoctorScheduleRequest doctorScheduleRequest = new DoctorScheduleRequest();
        doctorScheduleRequest.setId(doctorId);
        model.addAttribute("doctorScheduleRequest", doctorScheduleRequest);
        return "/doctor/scheduleForm";
    }

}
