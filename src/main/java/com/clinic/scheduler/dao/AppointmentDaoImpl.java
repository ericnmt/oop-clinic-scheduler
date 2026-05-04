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
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of AppointmentDao interface.
 * Directly interacts with database through SQL queries.
 */
@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Handles multiple date formats
    private final DateTimeFormatter multiFormatter = new DateTimeFormatterBuilder()
            .appendPattern("[yyyy-MM-dd]")
            .appendPattern("[dd/MM/yyyy]")
            .appendPattern("[MMMM d, yyyy]")
            .parseCaseInsensitive()
            .toFormatter(Locale.US);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Appointment> appointmentRowMapper;

    // Base query used for all read operations, involves JOIN as opposed to DAO injection
    public static final String SELECT_JOIN_BASE = """
            SELECT a.AppointmentID, a.StartTime, a.EndTime, a.Status, a.Reason,
                   p.PatientID, p.Name AS PatientName, p.DateOfBirth, p.ContactInfo, p.IsActive AS PatientIsActive,
                   pr.ProviderID, pr.Name AS ProviderName, pr.Specialty, pr.Location, pr.IsActive AS ProviderIsActive
            FROM Appointment a
            INNER JOIN Patient p ON a.PatientID = p.PatientID
            INNER JOIN Provider pr ON a.ProviderID = pr.ProviderID
            """;

    /**
     * Constructor for AppointmentDaoImpl.
     *
     * @param jdbcTemplate JDBC template used for SQL queries
     */
    public AppointmentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.appointmentRowMapper = (rs, rowNum) -> {
            // Build Patient object from joined columns
            Patient patient = new Patient(
                    rs.getInt("PatientID"),
                    rs.getString("PatientName"),
                    LocalDate.parse(rs.getString("DateOfBirth"), multiFormatter),
                    rs.getString("ContactInfo")
            );
            patient.setActive(rs.getInt("PatientIsActive") == 1);

            // Build Provider object from joined columns
            Provider provider = new Provider(
                    rs.getInt("ProviderID"),
                    rs.getString("ProviderName"),
                    rs.getString("Specialty"),
                    rs.getString("Location")
            );
            provider.setActive(rs.getInt("ProviderIsActive") == 1);

            // Build the Appointment object using the entities defined above
            Appointment appointment = new Appointment(
                    rs.getInt("AppointmentID"),
                    patient,
                    provider,
                    LocalDateTime.parse(rs.getString("StartTime"), timeFormatter),
                    LocalDateTime.parse(rs.getString("EndTime"), timeFormatter),
                    rs.getString("Reason")
            );

            appointment.setStatus(AppointmentStatus.valueOf(rs.getString("Status")));
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
        String sql = SELECT_JOIN_BASE + "WHERE a.AppointmentID = ?";

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
        String sql = SELECT_JOIN_BASE + "WHERE a.PatientID = ?";
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
        String sql = SELECT_JOIN_BASE + "WHERE a.ProviderId = ?";
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
        String sql = SELECT_JOIN_BASE + "WHERE a.StartTime >= ? AND a.StartTime <= ?";

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
        String sql = SELECT_JOIN_BASE + "WHERE a.Status = ?";
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