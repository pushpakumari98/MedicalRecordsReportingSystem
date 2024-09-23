package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="vw_get_patient_doctor_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetails {

    @Id
    @Column(name="patient_id")
    private Long patient_id;

    @Column(name="patient_name")
    private String patient_name;

    @Column(name="patient_age")
    private Integer patient_age;

    @Column(name="doctor_name")
    private String doctor_name;

    @Column(name="app_status")
    private String app_status;

    @Column(name="checkup_room")
    private Integer checkup_room;

    @Column(name="date_of_appointment")
    private Date date_of_appointment;

    @Column(name="day_of_appointment")
    private String dayOfAppointment;

    @Column(name="nurse_name")
    private String nurseName;}
