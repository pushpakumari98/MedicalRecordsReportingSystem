package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;
import java.util.List;

@Entity
@Table(name="tbl_doctor_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> workingdays;

    @Temporal(TemporalType.DATE)
    private List<Date> availabledate;

    private Integer checkuproom;

    @JsonIgnore
    @OneToOne(mappedBy = "schedule")
    private Doctor doctor;

}
