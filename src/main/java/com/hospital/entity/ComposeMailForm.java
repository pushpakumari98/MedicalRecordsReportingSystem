package com.hospital.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComposeMailForm {

    private Long patientId;

    @NotBlank(message = "To Email is required")
    private String email;

    private String subject;

    private String message;

}
