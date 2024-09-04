package com.hospital.repository;

import com.hospital.entity.AppointmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentDetails, Long> {
}
