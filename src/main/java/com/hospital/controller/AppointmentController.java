package com.hospital.controller;

import com.hospital.constants.AppConstants;
import com.hospital.dto.AppRequest;

import com.hospital.entity.AppointmentDetails;
import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorSchedule;
import com.hospital.entity.Patient;
import com.hospital.repository.DoctorScheduleRepository;
import com.hospital.service.EmailService;
import com.hospital.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private DoctorScheduleRepository appointmentRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    EmailService emailService;



    //date, patientid, docid

    @PostMapping("/bookappointment")
    public ResponseEntity<String> bookPatientAppointment(@Valid @RequestBody AppRequest appRequest) throws ParseException {

        Patient patient = null;
        try {
            patient = patientService.getPatientByPatientId(appRequest.getPatientId());
        }catch (NoSuchElementException e){
            return ResponseEntity.ok().body("Patient Does Not Exist!");
        } catch (Exception e) {
            return ResponseEntity.ok().body("Something Went Wrong!");
        }

        Doctor doctor = patient.getDoctor();

        DoctorSchedule docSchedule = doctor.getSchedule();

        AppointmentDetails appointmentDetails = new AppointmentDetails();   //creating appointment details object
        appointmentDetails.setDateOfAppointment(appRequest.getAppointmentDate());
        appointmentDetails.setAppointmentStatus(AppConstants.PENDING);

        Date appointmentDate = appRequest.getAppointmentDate();
        Format f = new SimpleDateFormat("EEEE");        //logic to find day from a date
        String str = f.format(appointmentDate);

        appointmentDetails.setDayOfAppointment(str);
        appointmentDetails.setDocName(doctor.getName());
        appointmentDetails.setCheckupRoom(docSchedule.getCheckuproom());
        appointmentDetails.setPatientId(patient.getId());
        appointmentDetails.setDocId(doctor.getId());
        patient.setAppDetails(appointmentDetails);


        List<Date> docAvailableDate =  docSchedule.getAvailabledate();   //all the date    1/2/3/4  //3

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String patAppointmentDate = formatter.format(appRequest.getAppointmentDate());
        System.out.println(patAppointmentDate);

        //List<Date> dates = getDates(date1, date2);

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
        patient.setDoctor(doctor);
        patientService.savePatient(patient);

        //mail all the details to patient //krpi@gmail.com -> krpi@gmail.com
        String message = "Hi "+patient.getName()+","+"\n\nYour appoinment has been scheduled successfully. Find the appoinment details below."+
                "\n\nAppoinment No: 123"+
                "\n\nDoctor Name: "+doctor.getName();

        try {
            emailService.sendEmail("pushpa20052002kumari@gmail.com", "Appoinment Details",
                    message);
            System.out.println("email sent");
        }catch (Exception e){
            System.out.println("email send failed");
        }



        return ResponseEntity.ok().body("Success");
    }

    @GetMapping("/createDoctorSchedule")
    public String createDoctorSchedule(){

        Date today = new Date();
        Calendar last = Calendar.getInstance();
        last.setTime(today);
        last.add(Calendar.MONTH, 1);
        last.set(Calendar.DAY_OF_MONTH, 1);
        last.add(Calendar.DATE, -1);
        Date lastDayOfMonth = last.getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDayOfMonth);
        cal.add(Calendar.DATE, 1);

       Date firstDayOfNextMonth = cal.getTime();
        System.out.println("firstDayOfNextMonth "+firstDayOfNextMonth);




        Map<String, List<Date>> dateList = new HashMap<String, List<Date>>();

        while (today.before(firstDayOfNextMonth)) {
            Format f = new SimpleDateFormat("EEEE");
            String str = f.format(today);
            if(str.equalsIgnoreCase("Monday")){
                if(dateList.get("Monday")!=null){
                    List<Date> monDateList = dateList.get("Monday");
                    monDateList.add(today);
                    dateList.put("Monday", monDateList);
                } else if (dateList.get("Monday")==null) {
                    List<Date> monDateList = new ArrayList<>();
                    monDateList.add(today);
                    dateList.put("Monday", monDateList);
                }
            }else if(str.equalsIgnoreCase("Tuesday")){
                if(dateList.get("Tuesday")!=null){
                    List<Date> tueDateList = dateList.get("Tuesday");
                    tueDateList.add(today);
                    dateList.put("Tuesday", tueDateList);
                } else if (dateList.get("Tuesday")==null) {
                    List<Date> tueDateList = new ArrayList<>();
                    tueDateList.add(today);
                    dateList.put("Tuesday", tueDateList);
                }
            }else if(str.equalsIgnoreCase("Wednesday")){
                if(dateList.get("Wednesday")!=null){
                    List<Date> wedDateList = dateList.get("Wednesday");
                    wedDateList.add(today);
                    dateList.put("Wednesday", wedDateList);
                } else if (dateList.get("Wednesday")==null) {
                    List<Date> wedDateList = new ArrayList<>();
                    wedDateList.add(today);
                    dateList.put("Wednesday", wedDateList);
                }
            }else if(str.equalsIgnoreCase("Thursday")){
                if(dateList.get("Thursday")!=null){
                    List<Date> thuDateList = dateList.get("Thursday");
                    thuDateList.add(today);
                    dateList.put("Thursday", thuDateList);
                } else if (dateList.get("Thursday")==null) {
                    List<Date> thuDateList = new ArrayList<>();
                    thuDateList.add(today);
                    dateList.put("Thursday", thuDateList);
                }
            }else if(str.equalsIgnoreCase("Friday")){
                if(dateList.get("Friday")!=null){
                    List<Date> friDateList = dateList.get("Friday");
                    friDateList.add(today);
                    dateList.put("Friday", friDateList);
                } else if (dateList.get("Friday")==null) {
                    List<Date> friDateList = new ArrayList<>();
                    friDateList.add(today);
                    dateList.put("Friday", friDateList);
                }
            }else if(str.equalsIgnoreCase("Saturday")){
                if(dateList.get("Saturday")!=null){
                    List<Date> satDateList = dateList.get("Saturday");
                    satDateList.add(today);
                    dateList.put("Saturday", satDateList);
                } else if (dateList.get("Saturday")==null) {
                    List<Date> satDateList = new ArrayList<>();
                    satDateList.add(today);
                    dateList.put("Saturday", satDateList);
                }
            }else if(str.equalsIgnoreCase("Sunday")) {
                if(dateList.get("Sunday")!=null){
                    List<Date> sunDateList = dateList.get("Sunday");
                    sunDateList.add(today);
                    dateList.put("Sunday", sunDateList);
                } else if (dateList.get("Sunday")==null) {
                    List<Date> sunDateList = new ArrayList<>();
                    sunDateList.add(today);
                    dateList.put("Sunday", sunDateList);
                }
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, 1);
            today = calendar.getTime();
        }

        List<DoctorSchedule> appList = appointmentRepository.findAll();
        for(DoctorSchedule app : appList){
            List<String> dayList = app.getWorkingdays();
            List<Date> dateList1 = new ArrayList<>();
            for(String day:dayList){
                if(day.equalsIgnoreCase("MON")){
                    if(dateList.get("Monday")!=null){
                        dateList1.addAll(dateList.get("Monday"));
                    }
                }else if(day.equalsIgnoreCase("TUE")){
                    if(dateList.get("Tuesday")!=null){
                        dateList1.addAll(dateList.get("Tuesday"));
                    }
                }else if(day.equalsIgnoreCase("WED")){
                    if(dateList.get("Wednesday")!=null){
                        dateList1.addAll(dateList.get("Wednesday"));
                    }
                }else if(day.equalsIgnoreCase("THU")){
                    if(dateList.get("Thursday")!=null){
                        dateList1.addAll(dateList.get("Thursday"));
                    }
                }else if(day.equalsIgnoreCase("FRI")){
                    if(dateList.get("Friday")!=null){
                        dateList1.addAll(dateList.get("Friday"));
                    }
                }else if(day.equalsIgnoreCase("SAT")){
                    if(dateList.get("Saturday")!=null){
                        dateList1.addAll(dateList.get("Saturday"));
                    }
                }else if(day.equalsIgnoreCase("SUN")){
                    if(dateList.get("Sunday")!=null){
                        dateList1.addAll(dateList.get("Sunday"));
                    }
                }
            }
            app.setAvailabledate(dateList1);

            appointmentRepository.save(app);
        }

        return "Appointment has been scheduled successfully";
    }
}
