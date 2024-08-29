package com.hospital.serviceimpl;

import com.hospital.entity.DoctorHistory;
import com.hospital.repository.DoctorHistoryRepository;
import com.hospital.service.DoctorHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorHistoryServiceImpl implements DoctorHistoryService {
    @Autowired
    DoctorHistoryRepository doctorHistoryRepository;
    @Override
    public DoctorHistory saveDeletedDoctor(DoctorHistory doctor) {
        return doctorHistoryRepository.save(doctor);
    }
}
