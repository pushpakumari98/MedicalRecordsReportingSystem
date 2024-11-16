package com.hospital.controller;

import com.hospital.constants.AppConstants;
import com.hospital.dto.DoctorRequest;
import com.hospital.dto.DoctorScheduleRequest;
import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorSchedule;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorScheduleRepository;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/doctor/ui")
@Controller
public class DoctorViewController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorScheduleRepository doctorScheduleRepository;

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
        return "redirect:/doctor/ui/addDoctorForm";
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

        Doctor doctor = doctorService.findById(doctorId);

        doctorScheduleRequest.setName(doctor.getName());
        List<String> workingDAYS = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

        model.addAttribute("doctorScheduleRequest", doctorScheduleRequest);
        model.addAttribute("workingdays", workingDAYS);
        return "/doctor/scheduleForm";
    }

    @PostMapping("/saveSchedule")
    public String saveSchedule(Model model, @ModelAttribute("doctorScheduleRequest") DoctorScheduleRequest doctorScheduleRequest,
                               RedirectAttributes redirectAttributes){

        System.out.println("test");
        //TODO: Validate that request has the correct days
        Doctor doctor = null;
        try {
            try {
                doctor = doctorService.getDoctorById(doctorScheduleRequest.getId());

            } catch (NoSuchElementException e) {
                redirectAttributes.addFlashAttribute("message", "Something went wrong.");
                redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something went wrong.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-danger");
        }
        DoctorSchedule schedule = new DoctorSchedule();
        if(doctor!=null && doctor.getSchedule()!=null){
            schedule = doctor.getSchedule();
        }
        schedule.setWorkingdays(doctorScheduleRequest.getWorkingdays());
        schedule.setCheckuproom(doctorScheduleRequest.getCheckuproom());
        doctor.setSchedule(schedule);
        Doctor doc = doctorService.saveDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "Schedule has been saved/updated.");
        redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");
        return "redirect:/doctor/ui/doctorList/0";
    }

    @GetMapping("/createAvaileableDate")
    public String addDoctorSchedule(Model model,
                                    @RequestParam(value = "doctorId", required = true) Long doctorId,
                                    RedirectAttributes redirectAttributes){

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

        Doctor doctor = doctorService.findById(doctorId);
        DoctorSchedule docSchedule = doctor.getSchedule();
        List<DoctorSchedule> appList = new ArrayList<>();
        if(docSchedule==null) {
            redirectAttributes.addFlashAttribute("message", "Please Add Doctor Schedule First.");
            redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");
            return "redirect:/doctor/ui/doctorList/0";
        }
        appList.add(docSchedule);
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

            doctorScheduleRepository.save(app);
        }

        redirectAttributes.addFlashAttribute("message", "Doctor availability has been updated.");
        redirectAttributes.addFlashAttribute("alertClass", "alert alert-success");
        return "redirect:/doctor/ui/doctorList/0";

    }

}
