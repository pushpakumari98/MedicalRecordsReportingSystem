package com.hospital.repository;

import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorHistoryRepository extends JpaRepository<DoctorHistory, Long> {


}
