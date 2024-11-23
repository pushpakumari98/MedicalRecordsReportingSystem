package com.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class CommonPageNavController {

    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

}
