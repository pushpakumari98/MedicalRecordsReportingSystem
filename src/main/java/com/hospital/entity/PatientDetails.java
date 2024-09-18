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
    private Long id;

    @Column(name="patient_name")
    private String patName;

    @Column(name="patient_age")
    private Integer age;

    @Column(name="doctor_name")
    private String doctorName;

    @Column(name="app_status")
    private String appStatus;

    @Column(name="checkup_room")
    private Integer checkupRoom;

    @Column(name="date_of_appoinment")
    private Date dateOfAppointment;

    @Column(name="day_of_appoinment")
    private String dayOfAppointment;

    @Column(name="nurse_name")
    private String nurseName;}
