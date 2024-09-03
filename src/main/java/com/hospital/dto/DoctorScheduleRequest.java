package com.hospital.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.entity.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleRequest {

    private Long id;

    private List<String> workingdays;

    private Integer checkuproom;

    @JsonIgnore
    @OneToOne(mappedBy = "schedule")
    private Doctor doctor;

}
