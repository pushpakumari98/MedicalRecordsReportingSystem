
package com.hospital.serviceimpl;
import com.hospital.entity.NurseHistory;
import com.hospital.repository.NurseHistoryRepository;
import com.hospital.service.NurseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NurseHistoryServiceImpl implements NurseHistoryService {

    @Autowired
    NurseHistoryRepository nurseHistoryRepository;

    @Override
    public NurseHistory saveDeletedNurse(NurseHistory nurse) {
        return nurseHistoryRepository.save(nurse);
    }
}
