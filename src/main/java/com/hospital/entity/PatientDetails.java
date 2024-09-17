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
    @Column(name="PATIENT_ID")
    private Long id;

    @Column(name="PATIENT_NAME")
    private String patName;

    @Column(name="PATIENT_AGE")
    private Integer age;

    @Column(name="DOCTOR_NAME")
    private String doctor_name;

    @Column(name="APP_STATUS")
    private String appointment_status;

    @Column(name="CHECKUP_ROOM")
    private Integer checkupRoom;

    @Column(name="DATE_OF_APPOINTMENT")
    private Date dateOfAppointment;

    @Column(name="DAY_OF_APPOINTMENT")
    private String dayOfAppointment;

    @Column(name="NURSE_NAME")
    private String nurseName;

}
