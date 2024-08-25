package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    @NotBlank(message="This is a mandatory field")
    private String city;

    @NotBlank(message="This is mandatory field")
    private String state;

    @NotBlank(message="This is mandatory field")
    private String country;

    @NotNull(message = "Pin is a mandatory field.")
    private Long pin;

}
