package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NurseRequest {

    @NotBlank(message = "Name is a mandatory field.")
    private String name;

     @NotNull(message = "Age is a mandatory field.")
    private Integer age;

     @NotNull(message = "Phone is a mandatory field.")
     private Long phone;

     @NotNull(message="DOB is a mandatory field")
     private Date dob;

}
