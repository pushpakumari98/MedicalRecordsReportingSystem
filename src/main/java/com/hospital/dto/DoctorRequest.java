package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "Name is a mandatory field.")
    private String name;

   // @NotNull(message = "Age is a mandatory field.")
    private Integer age;

   // @NotBlank(message = "Phone is a mandatory field.")
   // @Size(min = 10, message = "Minimum 10 digits required")
    private Long phone;

  //  @NotNull(message="DOB is a mandatory field")
    private Date dob;

   // @NotNull(message ="It's mandatory field")
    private String specialist;
}
