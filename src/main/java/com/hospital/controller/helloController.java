package com.hospital.controller;


import com.hospital.dto.DoctorRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


//@Controller vs @RestController
@Controller
@RequestMapping("/api1")
public class helloController {

    @GetMapping("/hello")
    public String hello(Model model){

        DoctorRequest docForm = new DoctorRequest();
        model.addAttribute("docForm",docForm);
        return "hello";
    }

    @PostMapping("/addPatient")
    public String addPatient(@Valid @ModelAttribute DoctorRequest docRequest, BindingResult result, HttpSession session){

        if(result.hasErrors()){
            return "hello";
        }

        System.out.println(docRequest.getName());
            //sasa


        return "hello";
    }

}
