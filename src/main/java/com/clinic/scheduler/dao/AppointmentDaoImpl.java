package com.clinic.scheduler.dao;

import com.clinic.scheduler.model.*;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementation of AppointmentDao interface. Directly interacts with database through SQL queries.
 */
@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    // Formatter for LocalDateTime
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Inject JDBC Template
    private final JdbcTemplate jdbcTemplate;

    // Inject PatientDao and ProviderDao to reconstruct Patient/Provider objects
    private final PatientDao patientDao;
    private final ProviderDao providerDao;

    // Initialize row mapper, construct when calling the class constructor.
    private final RowMapper<Appointment> appointmentRowMapper;

    /**
     * Constructor for the AppointmentDao Implementation object.
     *
     * @param jdbcTemplate to inject
     * @param patientDao to inject
     * @param providerDao to inject
     */
    public AppointmentDaoImpl(JdbcTemplate jdbcTemplate, PatientDao patientDao, ProviderDao providerDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.patientDao = patientDao;
        this.providerDao = providerDao;

        // Initialize RowMapper
        this.appointmentRowMapper = (rs, rowNum) -> {
            // Sets row values in order of: appointmentId, startTime, endTime, status, reason, patientId, providerId
            int appointmentId = rs.getInt("AppointmentID");
            LocalDateTime startTime = LocalDateTime.parse(rs.getString("StartTime"), timeFormatter);
            LocalDateTime endTime = LocalDateTime.parse(rs.getString("EndTime"), timeFormatter);
            AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("Status"));
            String reason = rs.getString("Reason");

            // Retrieve foreign keys from Patient and Provider tables
            int patientId = rs.getInt("PatientID");
            int providerId = rs.getInt("ProviderID");

            Patient patient = patientDao.getPatientById(patientId);
            Provider provider = providerDao.getProviderById(providerId);

            Appointment appt = new Appointment(appointmentId, patient, provider, startTime, endTime, reason);
            appt.setStatus(status);

            return appt;
        };
    }

    /**
     * Adds an Appointment object to the database.
     *
     * @param appointment to be added
     */
    @Override
    public void createAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointment (StartTime, EndTime, Status, Reason, PatientID, ProviderID) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                appointment.getStartDateTime().format(timeFormatter),
                appointment.getEndDateTime().format(timeFormatter),
                appointment.getStatus().name(),
                appointment.getReason(),
                appointment.getPatient().getPatientId(),
                appointment.getProvider().getProviderId()
        );
    }

    /**
     * Searches for an appointment by a specified ID.
     *
     * @param appointmentId to search for
     * @return matching Appointment
     */
    @Override
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM Appointment WHERE AppointmentID = ?";
        return jdbcTemplate.queryForObject(sql, appointmentRowMapper, appointmentId);
    }

    /**
     * Searches for appointments filtered by a Patient (patientId).
     *
     * @param patientId of Patient
     * @return list of matching appointments by Patient
     */
    @Override
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        String sql = "SELECT * FROM Appointment WHERE PatientID = ?";
        return jdbcTemplate.query(sql, appointmentRowMapper, patientId);
    }

    /**
     * Searches for appointments filtered by a Provider (providerId).
     *
     * @param providerId of Provider
     * @return list of matching appointments by Provider
     */
    @Override
    public List<Appointment> getAppointmentsByProvider(int providerId) {
        String sql = "SELECT * FROM Appointment WHERE ProviderID = ?";
        return jdbcTemplate.query(sql, appointmentRowMapper, providerId);
    }

    /**
     * Searches for appointments filtered by a given time range.
     *
     * @param startDate of range
     * @param endDate of range
     * @return list of appointments within time range
     */
    @Override
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM Appointment WHERE StartTime >= ? AND StartTime <= ?";

        // Define START of day and END of day
        String startString = startDate.atStartOfDay().format(timeFormatter);
        String endString = endDate.atTime(23, 59, 59).format(timeFormatter);

        return jdbcTemplate.query(sql, appointmentRowMapper, startString, endString);
    }

    /**
     * Searches for appointments filtered by a given status.
     *
     * @param status of appointment to search for
     * @return list of all appointments matching the given status
     */
    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        String sql = "SELECT * FROM Appointment WHERE Status = ?";
        return jdbcTemplate.query(sql, appointmentRowMapper, status.name());
    }

    /**
     * Update information for an appointment.
     *
     * @param appointment to update
     */
    @Override
    public void updateAppointment(Appointment appointment) {
        String sql = "UPDATE Appointment SET StartTime = ?, EndTime = ?, Status = ?, Reason = ? WHERE AppointmentID = ?";
        jdbcTemplate.update(sql,
                appointment.getStartDateTime().format(timeFormatter),
                appointment.getEndDateTime().format(timeFormatter),
                appointment.getStatus().name(),
                appointment.getReason(),
                appointment.getAppointmentId()
        );
    }

    /**
     * Delete an appointment from the database.
     *
     * @param appointmentId of appointment to delete
     */
    @Override
    public void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointment WHERE AppointmentID = ?";
        jdbcTemplate.update(sql, appointmentId);
    }
}