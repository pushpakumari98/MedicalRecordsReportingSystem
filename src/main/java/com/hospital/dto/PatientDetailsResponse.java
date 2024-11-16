package com.hospital.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
@Getter
@Setter
public class PatientDetailsResponse {

    private Long patient_id;

    private String patient_name;

    private Integer patient_age;

    private String doctor_name;

    private String app_status;

    private Integer checkup_room;

    private Date date_of_appointment;

    private String dayOfAppointment;

    private String nurseName;

    public PatientDetailsResponse() {
    }

    public PatientDetailsResponse(Long patient_id, String patient_name, Integer patient_age, String doctor_name, String app_status, Integer checkup_room, Date date_of_appointment, String dayOfAppointment, String nurseName) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.patient_age = patient_age;
        this.doctor_name = doctor_name;
        this.app_status = app_status;
        this.checkup_room = checkup_room;
        this.date_of_appointment = date_of_appointment;
        this.dayOfAppointment = dayOfAppointment;
        this.nurseName = nurseName;
    }
}