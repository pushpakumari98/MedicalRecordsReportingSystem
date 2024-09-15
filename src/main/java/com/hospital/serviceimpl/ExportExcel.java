package com.hospital.serviceimpl;

import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.util.CellUtil.createCell;

@Service
public class ExportExcel {

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    public ExportExcel(){
        workbook = new XSSFWorkbook();
    }

    public void createCells(Row row, int columnCount, Object data, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(data !=null){
            if(data instanceof Integer){
                cell.setCellValue((Integer) data);
            }if(data instanceof Double){
                cell.setCellValue((Double) data);
            }if(data instanceof String){
                cell.setCellValue((String) data);
            }if(data instanceof Long){
                cell.setCellValue((Long) data);
            }if(data instanceof Boolean){
                cell.setCellValue((Boolean) data);
            }if(data instanceof Date) {
                cell.setCellValue((Date) data);
            }
        }
        cell.setCellStyle(style);
    }

    private void createPatientHeaderRow(){
        sheet   = workbook.createSheet("Patient Information");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Patient Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Patient ID", style);
        createCell(row, 1, "Patient Name", style);
        createCell(row, 2, "Patient DOB", style);
        createCell(row, 3, "Admission date", style);
        createCell(row, 4, "Age", style);
        createCell(row, 5, "Phone", style);
    }

    private void writePatientData(){

        List<Patient> patientList = patientService.getAllPatient();

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Patient patient : patientList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, patient.getId().toString(), style);
            createCell(row, columnCount++, patient.getName(), style);
            createCell(row, columnCount++, patient.getDob().toString(), style);
            createCell(row, columnCount++, patient.getAddmissionDate().toString(), style);
            createCell(row, columnCount++, patient.getAge().toString(), style);
            createCell(row, columnCount++, patient.getPhone(), style);

        }

    }

    public void exportPatientDataToExcel(HttpServletResponse response) throws IOException {
        createPatientHeaderRow();
        writeDoctorData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
    private void createDoctorHeaderRow(){
        sheet   = workbook.createSheet("Doctor Information");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Doctor Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Doctor ID", style);
        createCell(row, 1, "Doctor Name", style);
        createCell(row, 2, "Doctor DOB", style);
        createCell(row,3,"Doctor Specialist",style);
        createCell(row, 4, "Schedule ID", style);
        createCell(row, 5, "Age", style);
        createCell(row, 6, "Phone", style);
    }

    private void writeDoctorData(){

        List<Doctor> doctorList = doctorService.getAllDoctor();

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Doctor doctor : doctorList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, doctor.getId().toString(), style);
            createCell(row, columnCount++, doctor.getName(), style);
            createCell(row, columnCount++, doctor.getDob().toString(), style);
            createCell(row, columnCount++, doctor.getSpecialist().toString(), style);
            createCell(row,columnCount++,doctor.getSchedule().toString(),style);
            createCell(row, columnCount++, doctor.getAge().toString(), style);
            createCell(row, columnCount++, doctor.getPhone().toString(), style);

        }

    }

    public void exportDoctorDataToExcel(HttpServletResponse response) throws IOException {
        createDoctorHeaderRow();
        writeDoctorData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

}
