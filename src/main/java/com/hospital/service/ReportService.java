package com.hospital.service;

import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public interface ReportService {

    public String exportPatientDetails(String reportFormat) throws FileNotFoundException, JRException;

}
