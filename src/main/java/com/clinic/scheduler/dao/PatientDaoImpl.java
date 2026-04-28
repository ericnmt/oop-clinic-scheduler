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
 * Implementation of PatientDao interface. Directly interacts with database through SQL queries.
 */
@Repository
public class PatientDaoImpl implements PatientDao {

    // Inject JDBCTemplate
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to inject jdbc template using the above definition.
     *
     * @param jdbcTemplate to inject
     */
    public PatientDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Helper to handle Date of Birth formatting (String to LocalDate object)
    DateTimeFormatter multiFormatter = new DateTimeFormatterBuilder()
            .appendPattern("[yyyy-MM-dd]")
            .appendPattern("[dd/MM/yyyy]")
            .appendPattern("[MMMM d, yyyy]")
            .parseCaseInsensitive()
            .toFormatter(Locale.US);

    /**
     * Method to map patient's instance fields to DB table columns (defined in schema.sql)
     */
    private final RowMapper<Patient> patientRowMapper = (rs, rowNum) -> {
        int patientId = rs.getInt("PatientID");
        String name = rs.getString("Name");
        LocalDate dateOfBirth = LocalDate.parse(rs.getString("DateOfBirth"), multiFormatter);
        String contactInfo = rs.getString("ContactInfo");

        // Return Patient object constructed from above database row
        return new Patient(patientId, name, dateOfBirth, contactInfo);


    };

    /**
     * Create a patient, map it the Database.
     *
     * @param patient to be created
     */
    @Override
    public void createPatient(Patient patient) {
        String sql = "INSERT INTO Patient (PatientID, Name, DateOfBirth, Contact Info) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, patient.getPatientId(), patient.getName(), patient.getDateOfBirth().toString(), patient.getContactInfo());
    }

    /**
     * Filter a Patient by their ID.
     *
     * @param patientId to search for
     * @return Patient object that matches patientId provided
     */
    @Override
    public Patient getPatientById(int patientId) {
        String sql = "SELECT * FROM Patient WHERE PatientID = ?";
        return jdbcTemplate.queryForObject(sql, patientRowMapper, patientId);
    }

    /**
     * Get all Patients from the database
     *
     * @return list of all Patient objects recorded.
     */
    @Override
    public List<Patient> getAllPatients() {
        String sql = "SELECT * FROM Patient";
        return jdbcTemplate.query(sql, patientRowMapper);
    }

    /**
     * Update a Patient's information.
     * Sorts through database by patientId.
     *
     * @param patient to update
     */
    @Override
    public void updatePatient(Patient patient) {
        String sql = "UPDATE Patient SET Name = ?, DateOfBirth = ?, ContactInfo = ?, WHERE PatientID = ?";
        jdbcTemplate.update(sql, patient.getName(), patient.getDateOfBirth().toString(), patient.getContactInfo(), patient.getPatientId());
    }

    /**
     * Remove a patient from the database.
     *
     * @param patientId of patient to remove
     */
    @Override
    public void deletePatient(int patientId) {
        String sql = "DELETE FROM Patient WHERE PatientID = ?";
        jdbcTemplate.update(sql, patientId);
    }
}
