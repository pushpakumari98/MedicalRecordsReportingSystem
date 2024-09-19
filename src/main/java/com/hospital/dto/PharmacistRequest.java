package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PharmacistRequest {
    private Integer Id;
    private String name;
    private Integer age;
    private String specialization;
    private Date dob;
}
