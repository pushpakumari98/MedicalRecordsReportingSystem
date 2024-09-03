package com.hospital.controller;

import com.hospital.dto.AppRequest;

import com.hospital.entity.DoctorSchedule;

import com.hospital.repository.DoctorScheduleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private DoctorScheduleRepository appointmentRepository;

    //date, patientid, docid

    @PostMapping("/bookappointment")
    public ResponseBody bookPatientAppointment(@Valid @RequestBody AppRequest appRequest){


        return null;
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
