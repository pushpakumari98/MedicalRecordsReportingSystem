package com.hospital.repository;


import com.hospital.entity.NurseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseHistoryRepository extends JpaRepository<NurseHistory,Long> {
}
