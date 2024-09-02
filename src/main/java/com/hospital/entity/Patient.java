package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="tbl_patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="aadhar", nullable = false, unique = true)
    private String aadhar;

    @Column(name="name")
    private String name;

    private Integer age;

    private String phone;

    private Date dob;

    private Date addmissionDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id", referencedColumnName = "id")
    private AppointmentDetails appDetails;

}
