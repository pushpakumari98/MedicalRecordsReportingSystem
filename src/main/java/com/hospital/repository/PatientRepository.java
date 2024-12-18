package com.hospital.repository;

import com.hospital.entity.Patient;
import com.hospital.entity.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByAadhar(String aadhar);  //select id, age,  from tbl_patient where aadhar = "";

   // List<Patient> findByUser(PageRequest pageable);

   // @Query("SELECT * FROM tbl_patient patient")
   // List<Patient> findAllPatientDetails();
}
