package com.hospital.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatientRequest {
    @NotBlank(message = "Name is a mandatory field.")
    private String name;

    @NotNull(message = "Age is a mandatory field.")
    private Integer age;
    @NotBlank(message = "Phone is a mandatory field.")
    @Size(min = 2, message = "Minimum 2 digits required")
    private String phone;

    @NotNull(message="DOB is a mandatory field")
    private Date dob;
    @NotBlank(message="Fill your city name to proceed")
    private String city;
    @NotBlank(message="Fill your state name to proceed")
    private String state;

    @NotBlank(message="fill country name to proceed")
    private String country;

    @NotBlank(message ="Aadhar is a mandatory input." )
    private String aadhar;
}
