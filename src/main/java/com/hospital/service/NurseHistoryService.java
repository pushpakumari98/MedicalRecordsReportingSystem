
package com.hospital.service;

import com.hospital.entity.NurseHistory;
import org.springframework.stereotype.Service;

@Service
public interface NurseHistoryService {
    public NurseHistory saveDeletedNurse(NurseHistory nurse);
}
