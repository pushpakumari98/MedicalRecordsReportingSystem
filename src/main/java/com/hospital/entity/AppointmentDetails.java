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
@Table(name="TBL_ADDRESS")
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
    private Long patientId;
    private List<String> dayOfAppointment;
    private List<Date> dateOfAppointment;
    private String appointmentStatus;
    private Integer checkupRoom;

    @JsonIgnore
    @OneToOne(mappedBy = "appDetails")
    private Patient patient;

}
