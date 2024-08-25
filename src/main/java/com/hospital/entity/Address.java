package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.Doc;

@Entity
@Table(name="TBL_ADDRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

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
    private Patient patient;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private Doctor doctor;

}
