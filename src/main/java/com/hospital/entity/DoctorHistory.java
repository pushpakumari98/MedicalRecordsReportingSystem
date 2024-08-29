package com.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="tbl_doctor_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="doc_name")
    private String name;

    private Integer age;

    private Long phone;

    private Date dob;

    private String specialist;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressHistory address;


}
