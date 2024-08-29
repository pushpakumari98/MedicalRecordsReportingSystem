package com.hospital.service;

import com.hospital.entity.DoctorHistory;
import org.springframework.stereotype.Service;

@Service
public interface DoctorHistoryService {
    public DoctorHistory saveDeletedDoctor(DoctorHistory doctor);
}
