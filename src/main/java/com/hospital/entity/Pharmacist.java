package com.hospital.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Pharmacist {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private int age;
        private String specialization;
        private Date dob;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

}

