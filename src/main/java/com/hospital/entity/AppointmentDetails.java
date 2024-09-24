package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="TBL_APPOINMENT_DETAILS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String docName;
    private Long docId;
    private Long patId;
    private String dayOfAppointment;

    @Temporal(TemporalType.DATE)
    private Date dateOfAppointment;
    private String appointmentStatus;
    private Integer checkupRoom;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

}
