package com.clinic.scheduler.dao;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.clinic.scheduler.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementation of AppointmentDao interface.
 * Directly interacts with database through SQL queries.
 */
@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final JdbcTemplate jdbcTemplate;
    private final PatientDao patientDao;
    private final ProviderDao providerDao;

    private final RowMapper<Appointment> appointmentRowMapper;

    /**
     * Constructor for AppointmentDaoImpl.
     *
     * @param jdbcTemplate JDBC template used for SQL queries
     * @param patientDao DAO used to retrieve patient objects
     * @param providerDao DAO used to retrieve provider objects
     */
    public AppointmentDaoImpl(JdbcTemplate jdbcTemplate,
                              PatientDao patientDao,
                              ProviderDao providerDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.patientDao = patientDao;
        this.providerDao = providerDao;

        this.appointmentRowMapper = (rs, rowNum) -> {
            int appointmentId = rs.getInt("AppointmentID");
            LocalDateTime startTime = LocalDateTime.parse(rs.getString("StartTime"), timeFormatter);
            LocalDateTime endTime = LocalDateTime.parse(rs.getString("EndTime"), timeFormatter);
            AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("Status"));
            String reason = rs.getString("Reason");

            int patientId = rs.getInt("PatientID");
            int providerId = rs.getInt("ProviderID");

            Patient patient = patientDao.getPatientById(patientId);
            Provider provider = providerDao.getProviderById(providerId);

            Appointment appointment = new Appointment(
                    appointmentId,
                    patient,
                    provider,
                    startTime,
                    endTime,
                    reason
            );

            appointment.setStatus(status);

            return appointment;
        };
    }

    /**
     * Adds an appointment to the database.
     *
     * @param appointment appointment to create
     */
    @Override
    public void createAppointment(Appointment appointment) {
        String sql = """
            INSERT INTO Appointment
            (StartTime, EndTime, Status, Reason, PatientID, ProviderID)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, appointment.getStartDateTime().format(timeFormatter));
            ps.setString(2, appointment.getEndDateTime().format(timeFormatter));
            ps.setString(3, appointment.getStatus().name());
            ps.setString(4, appointment.getReason());
            ps.setInt(5, appointment.getPatient().getPatientId());
            ps.setInt(6, appointment.getProvider().getProviderId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            appointment.setAppointmentId(key.intValue());
        }
    }

    /**
     * Gets an appointment by ID.
     *
     * @param appointmentId appointment ID to search for
     * @return matching appointment, or null if not found
     */
    @Override
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM Appointment WHERE AppointmentID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, appointmentRowMapper, appointmentId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Gets appointments by patient ID.
     *
     * @param patientId patient ID
     * @return list of matching appointments
     */
    @Override
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        String sql = "SELECT * FROM Appointment WHERE PatientID = ?";
        return jdbcTemplate.query(sql, appointmentRowMapper, patientId);
    }

    /**
     * Gets appointments by provider ID.
     *
     * @param providerId provider ID
     * @return list of matching appointments
     */
    @Override
    public List<Appointment> getAppointmentsByProvider(int providerId) {
        String sql = "SELECT * FROM Appointment WHERE ProviderID = ?";
        return jdbcTemplate.query(sql, appointmentRowMapper, providerId);
    }

    /**
     * Gets appointments within a date range.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of appointments within the range
     */
    @Override
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM Appointment WHERE StartTime >= ? AND StartTime <= ?";

        String startString = startDate.atStartOfDay().format(timeFormatter);
        String endString = endDate.atTime(23, 59, 59).format(timeFormatter);

        return jdbcTemplate.query(sql, appointmentRowMapper, startString, endString);
    }

    /**
     * Gets appointments by status.
     *
     * @param status appointment status
     * @return list of matching appointments
     */
    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        String sql = "SELECT * FROM Appointment WHERE Status = ?";
        return jdbcTemplate.query(sql, appointmentRowMapper, status.name());
    }

    /**
     * Updates an appointment in the database.
     *
     * @param appointment appointment with updated information
     */
    @Override
    public void updateAppointment(Appointment appointment) {
        String sql = """
                UPDATE Appointment
                SET StartTime = ?, EndTime = ?, Status = ?, Reason = ?
                WHERE AppointmentID = ?
                """;

        jdbcTemplate.update(sql,
                appointment.getStartDateTime().format(timeFormatter),
                appointment.getEndDateTime().format(timeFormatter),
                appointment.getStatus().name(),
                appointment.getReason(),
                appointment.getAppointmentId()
        );
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointmentId appointment ID to delete
     */
    @Override
    public void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointment WHERE AppointmentID = ?";
        jdbcTemplate.update(sql, appointmentId);
    }
}