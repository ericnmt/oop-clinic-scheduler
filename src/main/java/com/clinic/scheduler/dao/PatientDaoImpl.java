package com.clinic.scheduler.dao;

import com.clinic.scheduler.model.Patient;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of PatientDao interface.
 * Handles CRUD operations for Patient table.
 */
@Repository
public class PatientDaoImpl implements PatientDao {

    private final JdbcTemplate jdbcTemplate;

    public PatientDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Handles multiple date formats
    private final DateTimeFormatter multiFormatter = new DateTimeFormatterBuilder()
            .appendPattern("[yyyy-MM-dd]")
            .appendPattern("[dd/MM/yyyy]")
            .appendPattern("[MMMM d, yyyy]")
            .parseCaseInsensitive()
            .toFormatter(Locale.US);

    // Maps DB row TO Patient object
    private final RowMapper<Patient> patientRowMapper = (rs, rowNum) -> {
        int patientId = rs.getInt("PatientID");
        String name = rs.getString("Name");
        LocalDate dateOfBirth = LocalDate.parse(rs.getString("DateOfBirth"), multiFormatter);
        String contactInfo = rs.getString("ContactInfo");

        Patient patient = new Patient(patientId, name, dateOfBirth, contactInfo);
        patient.setActive(rs.getInt("IsActive") == 1);
        return patient;
    };

    /**
     * Create a patient in DB.
     */
    @Override
    public void createPatient(Patient patient) {
        String sql = "INSERT INTO Patient (PatientID, Name, DateOfBirth, ContactInfo, IsActive) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                patient.getPatientId(),
                patient.getName(),
                patient.getDateOfBirth().toString(),
                patient.getContactInfo(),
                patient.isActive() ? 1 : 0);
    }

    /**
     * Get patient by ID.
     */
    @Override
    public Patient getPatientById(int patientId) {
        String sql = "SELECT * FROM Patient WHERE PatientID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, patientRowMapper, patientId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null; // important fix
        }
    }

    /**
     * Get all patients.
     */
    @Override
    public List<Patient> getAllPatients() {
        String sql = "SELECT * FROM Patient WHERE IsActive =1";
        return jdbcTemplate.query(sql, patientRowMapper);
    }

    /**
     * Update patient info.
     */
    @Override
    public void updatePatient(Patient patient) {
        String sql = "UPDATE Patient SET Name = ?, DateOfBirth = ?, ContactInfo = ? WHERE PatientID = ?";
        jdbcTemplate.update(sql,
                patient.getName(),
                patient.getDateOfBirth().toString(),
                patient.getContactInfo(),
                patient.getPatientId()
        );
    }

    /**
     * Delete patient.
     */
    @Override
    public void deletePatient(int patientId) {
        String sql = "UPDATE Patient SET IsActive = 0 WHERE PatientID = ?";
        jdbcTemplate.update(sql, patientId);
    }
}
