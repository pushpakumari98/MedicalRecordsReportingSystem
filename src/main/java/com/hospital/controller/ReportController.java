package com.hospital.controller;

import com.hospital.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileNotFoundException;

@Controller("/ui")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report/{format}")
    public String generatePatientDetailsReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return reportService.exportPatientDetails(format);
    }

}
