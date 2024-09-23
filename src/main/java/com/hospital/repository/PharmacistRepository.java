package com.hospital.repository;

import com.hospital.entity.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist,Long> {
    Optional<Pharmacist> findByPhone(String phone);
}
