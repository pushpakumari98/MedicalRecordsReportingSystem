package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

    @Column(name="email")
    private String email;

    private Integer age;

    private String phone;

    private Date dob;

    private Date addmissionDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "patient",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Doctor> doctor;

    @OneToMany(mappedBy = "patient",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AppointmentDetails> appDetails;

}
