package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter

public class Pharmacist {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;
        private String name;
        private String age;
        private String specialization;
        private Date dob;
        private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="nurse_id",referencedColumnName = "id")
    private Nurse nurse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="doctor_id",referencedColumnName = "id")
    private Doctor doctor;

}

