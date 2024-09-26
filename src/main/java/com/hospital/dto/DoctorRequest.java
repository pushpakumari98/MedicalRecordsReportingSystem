package com.hospital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequest {

    private Long id;

    @NotBlank(message = "Name is a mandatory field.")
    private String name;

    @NotNull(message = "Age is a mandatory field.")
    private Integer age;

    @NotNull(message = "Phone is a mandatory field.")
    private Long phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message="DOB is a mandatory field")
    private Date dob;

   @NotBlank(message ="It's mandatory field")
    private String specialist;

    @NotBlank(message ="Add a valid email.")
   @Email(message = "Add a valid email")
   private String email;
}
