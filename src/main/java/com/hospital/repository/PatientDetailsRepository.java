package com.hospital.repository;

import com.hospital.entity.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDetailsRepository extends JpaRepository<PatientDetails,Long> {



}
