package com.hospital.service;

import org.springframework.stereotype.Service;

@Service
public interface AppointmentService {

    String updateAppointmentStatus(Long patientId);

}
