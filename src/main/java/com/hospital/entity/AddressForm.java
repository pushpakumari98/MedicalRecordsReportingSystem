package com.hospital.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressForm {

    private Long patientId;

    private Long addressId;

    private Long addId;

    //@NotBlank(message = "City is required")
    private String city;

    private String state;

    private String country;

    private Long pin;

    private String addType;

}
