package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TBL_ADDRESS_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String state;

    private String country;

    private Long pin;

    private String addType;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private PatientHistory patient;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private DoctorHistory doctor;

}
