
package com.hospital.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class AppRequest {
    @Temporal(TemporalType.DATE)
    @NotNull(message="This is mandatory field")
    private Date appoinmentDate;
    @NotNull(message="This is mandatory field")
    private Long patientId;
    @NotNull(message="This field is mandatory")
    private Long docId;
}
