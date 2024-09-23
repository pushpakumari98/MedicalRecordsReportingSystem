package com.hospital.serviceimpl;

import com.hospital.entity.PatientDetails;
import com.hospital.repository.PatientDetailsRepository;
import com.hospital.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    PatientDetailsRepository patientDetailsRepository;

    @Override
    public String exportPatientDetails(String reportFormat) throws FileNotFoundException, JRException {

        String reportPath = "C:\\Users\\kmrpi\\Desktop\\report";
        List<PatientDetails> patientDetailsList = patientDetailsRepository.findAll();
        File file = ResourceUtils.getFile("classpath:patientdetails.jrxml");
        System.out.println(file.getAbsolutePath());
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(patientDetailsList);
        Map<String, Object> map = new HashMap<>();
        map.put("CreatedBy","HMS");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,dataSource);

        if(reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,reportPath+"\\patientdetails.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,reportPath+"\\patientdetails.pdf");
        }



        return "Report Generated";
    }
}
