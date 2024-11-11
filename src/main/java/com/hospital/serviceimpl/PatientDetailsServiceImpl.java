package com.hospital.serviceimpl;

import com.hospital.dto.PatientDetailsResponse;
import com.hospital.service.PatientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientDetailsServiceImpl implements PatientDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PatientDetailsResponse> getPatientDetails() {
        return jdbcTemplate.query("SELECT * FROM fn_get_patient_details()",
                (rs, rowNum) -> new PatientDetailsResponse(
                        rs.getLong("patient_id"),
                        rs.getString("patient_name"),
                        rs.getInt("patient_age"),
                        rs.getString("doctor_name"),
                        rs.getString("app_status"),
                        rs.getInt("checkup_room"),
                        rs.getDate("date_of_appointment"),
                        rs.getString("dayOfAppointment"),
                        rs.getString("nurseName")
                )
        );
    }
}
