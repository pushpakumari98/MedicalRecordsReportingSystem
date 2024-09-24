package com.hospital.serviceimpl;

import com.hospital.entity.AppointmentDetails;
import com.hospital.entity.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.PatientDetailsRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public AppointmentDetails saveAppoinmentDetails(AppointmentDetails appoinment) {
        AppointmentDetails appointmentDetails = null;
        if(appoinment!=null){
             appointmentRepository.save(appoinment);
        }
        return appointmentDetails;
    }

    @Override
    public String updateAppointmentStatus(Long patientId) {
        Patient patient = patientRepository.findById(patientId).get();
        Long appId = patient.getAppDetails().get(0).getId();

        AppointmentDetails appDetails = appointmentRepository.findById(appId).get();
        appDetails.setAppointmentStatus("Success");
        appointmentRepository.save(appDetails);
        return "Success";
    }
}
