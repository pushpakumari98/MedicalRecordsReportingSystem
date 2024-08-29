package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="tbl_patient_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="aadhar", nullable = false, unique = false)
    private String aadhar;

    @Column(name="name")
    private String name;

    private Integer age;

    private String phone;

    private Date dob;

    private Date addmissionDate;

    private Date dischargeDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressHistory address;

}
