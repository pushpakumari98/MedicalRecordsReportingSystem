package com.hospital.service;

import com.hospital.entity.AppointmentDetails;
import org.springframework.stereotype.Service;

@Service
public interface AppointmentService {

    AppointmentDetails saveAppoinmentDetails(AppointmentDetails appoinment);

    String updateAppointmentStatus(Long patientId);

}
