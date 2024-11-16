package com.hospital.serviceimpl;

import com.hospital.entity.Doctor;
import com.hospital.entity.Nurse;
import com.hospital.entity.PatientDetails;
import com.hospital.entity.Pharmacist;
import com.hospital.repository.PatientDetailsRepository;
import com.hospital.service.DoctorService;
import com.hospital.service.NurseService;
import com.hospital.service.PatientService;
import com.hospital.service.PharmacistService;
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
    private PatientDetailsRepository patientDetailsRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;
    @Autowired
    private PharmacistService pharmacistService;


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
        workbook = new XSSFWorkbook();
        sheet   = workbook.createSheet("Patient Information");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Patient info", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Patient ID", style);
        createCell(row, 1, "Patient Name", style);
        createCell(row, 2, "Age", style);
        createCell(row, 3, "Doctor Name", style);
        createCell(row, 4, "Appointment Status", style);
        createCell(row, 5, "CheckUp Room", style);
        createCell(row, 6, "Date Of Appointment", style);
        createCell(row, 7, "Day of Appointment", style);
        createCell(row, 8, "Nurse Name", style);    
}

    private void writePatientData(){

        List<PatientDetails> patientList = patientDetailsRepository.findAll();

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (PatientDetails patient : patientList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, patient.getPatient_id().toString(), style);
            if(patient.getPatient_name()!=null)
            createCell(row, columnCount++, patient.getPatient_name(), style);
            if(patient.getPatient_age()!=null)
            createCell(row, columnCount++, patient.getPatient_age().toString(), style);
            if(patient.getDoctor_name()!=null)
            createCell(row, columnCount++, patient.getDoctor_name().toString(), style);
            if(patient.getApp_status()!=null)
            createCell(row, columnCount++, patient.getApp_status().toString(), style);
            if(patient.getCheckup_room()!=null)
                createCell(row, columnCount++, patient.getCheckup_room().toString(), style);
            if(patient.getDate_of_appointment()!=null)
                createCell(row, columnCount++, patient.getDate_of_appointment().toString(), style);
            if(patient.getDayOfAppointment()!=null)
                createCell(row, columnCount++, patient.getDayOfAppointment().toString(), style);
            if(patient.getNurseName()!=null)
                createCell(row, columnCount++, patient.getNurseName().toString(), style);


        }

    }

    public void exportPatientDataToExcel(HttpServletResponse response) throws IOException {
        createPatientHeaderRow();
        writePatientData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
    private void createDoctorHeaderRow(){
        workbook = new XSSFWorkbook();
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
            if(doctor.getSpecialist()!=null){
                createCell(row, columnCount++, doctor.getSpecialist().toString(), style);
            }else{
                createCell(row, columnCount++, null, style);
            }
            if(doctor.getSchedule()!=null){
                createCell(row,columnCount++,doctor.getSchedule().toString(),style);
            }else{
                createCell(row,columnCount++,null,style);
            }
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


    private void createNurseHeaderRow(){
        sheet   = workbook.createSheet("Nurse Information");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Nurse Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Nurse ID", style);
        createCell(row, 1, "Nurse Name", style);
        createCell(row, 2, "Age", style);
        createCell(row, 3, "Phone", style);
        createCell(row, 4, "Nurse DOB", style);

    }

    private void writeNurseData(){

            List<Nurse> nurseList = nurseService.getAllNurse();

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Nurse nurse : nurseList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, nurse.getId().toString(), style);
            createCell(row, columnCount++, nurse.getName(), style);
            createCell(row, columnCount++, nurse.getAge().toString(), style);
            createCell(row, columnCount++, nurse.getPhone().toString(), style);
            createCell(row, columnCount++, nurse.getDob().toString(), style);

        }

    }

    public void exportNurseDataToExcel(HttpServletResponse response) throws IOException {
        createNurseHeaderRow();
        writeNurseData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    private void createPharmacistHeaderRow(){
        sheet   = workbook.createSheet("Pharmacist Information");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Pharmacist Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Pharmacist ID", style);
        createCell(row, 1, "Pharmacist Name", style);
        createCell(row, 2, "Age", style);
        createCell(row, 3,"Specialization",style);
        createCell(row, 4, "Phone", style);
        createCell(row, 5, "Pharmacist DOB", style);

    }

    private void writePharmacistData(){

        List<Pharmacist> pharmacistList = pharmacistService.getAllPharmacists();

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Pharmacist pharmacist : pharmacistList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, pharmacist.getId().toString(), style);
            createCell(row, columnCount++, pharmacist.getName(), style);
            createCell(row,columnCount++, pharmacist.getAge().toString(),style);
            createCell(row,columnCount++,pharmacist.getSpecialization(),style);
            createCell(row, columnCount++, pharmacist.getPhone().toString(), style);
            createCell(row, columnCount++, pharmacist.getDob().toString(), style);

        }

    }

    public void exportPharmacistDataToExcel(HttpServletResponse response) throws IOException {
        createPharmacistHeaderRow();
        writePharmacistData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

}
