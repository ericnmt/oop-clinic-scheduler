package com.clinic.scheduler.service;

import com.clinic.scheduler.dao.AppointmentDao;
import com.clinic.scheduler.dao.PatientDao;
import com.clinic.scheduler.dao.ProviderDao;
import com.clinic.scheduler.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for the clinic scheduling system.
 * Handles business logic and delegates database operations to DAO classes.
 */
@Service
public class AppointmentManager {

    private final PatientDao patientDao;
    private final ProviderDao providerDao;
    private final AppointmentDao appointmentDao;

    /**
     * Constructor for DAO dependency injection.
     *
     * @param patientDao DAO for patient operations
     * @param providerDao DAO for provider operations
     * @param appointmentDao DAO for appointment operations
     */
    public AppointmentManager(PatientDao patientDao,
                              ProviderDao providerDao,
                              AppointmentDao appointmentDao) {
        this.patientDao = patientDao;
        this.providerDao = providerDao;
        this.appointmentDao = appointmentDao;
    }

    /**
     * Adds a patient to the database.
     *
     * @param patient patient to add
     */
    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        if (patientDao.getPatientById(patient.getPatientId()) != null) {
            throw new IllegalArgumentException("Patient ID already exists.");
        }

        patientDao.createPatient(patient);
    }

    /**
     * Adds a provider to the database.
     *
     * @param provider provider to add
     */
    public void addProvider(Provider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null.");
        }

        if (providerDao.getProviderById(provider.getProviderId()) != null) {
            throw new IllegalArgumentException("Provider ID already exists.");
        }

        providerDao.createProvider(provider);
    }

    /**
     * Schedules a new appointment.
     *
     * @param patientId patient ID
     * @param providerId provider ID
     * @param reason reason for appointment
     * @param startTime appointment start time
     * @param endTime appointment end time
     * @return created appointment object
     */
    public Appointment scheduleAppointment(int patientId,
                                           int providerId,
                                           String reason,
                                           LocalDateTime startTime,
                                           LocalDateTime endTime) {

        Patient patient = patientDao.getPatientById(patientId);
        if (patient == null || !patient.isActive()) {
            throw new IllegalArgumentException("Cannot schedule: Patient ID " + patientId + " does not exist.");
        }

        Provider provider = providerDao.getProviderById(providerId);
        if (provider == null || !provider.isActive()) {
            throw new IllegalArgumentException("Cannot schedule: Provider ID " + providerId + " does not exist.");
        }

        validateTimeRange(startTime, endTime);

        if (hasProviderConflict(providerId, startTime, endTime, -1)) {
            throw new IllegalStateException("Cannot schedule: Provider has a conflicting appointment.");
        }

        Appointment appointment = new Appointment(
                0,
                patient,
                provider,
                startTime,
                endTime,
                reason
        );

        appointmentDao.createAppointment(appointment);
        return appointment;
    }

    /**
     * Reschedules an existing appointment using DAO/database logic.
     *
     * @param appointmentId appointment ID
     * @param startTime new start time
     * @param endTime new end time
     * @return updated appointment
     */
    public Appointment rescheduleAppointment(int appointmentId,
                                             LocalDateTime startTime,
                                             LocalDateTime endTime) {

        Appointment appointment = appointmentDao.getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Cannot reschedule: Appointment ID " + appointmentId + " not found.");
        }

        if (!appointment.getPatient().isActive() || !appointment.getProvider().isActive()) {
            throw new IllegalStateException("Cannot reschedule: Associated patient or provider is inactive.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED ||
                appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reschedule completed or cancelled appointment.");
        }

        validateTimeRange(startTime, endTime);

        int providerId = appointment.getProvider().getProviderId();

        if (hasProviderConflict(providerId, startTime, endTime, appointmentId)) {
            throw new IllegalStateException("Cannot reschedule: Provider has a conflicting appointment.");
        }

        appointment.setStartDateTime(startTime);
        appointment.setEndDateTime(endTime);

        appointmentDao.updateAppointment(appointment);
        return appointment;
    }

    /**
     * Updates appointment status.
     *
     * @param appointmentId appointment ID
     * @param status new status
     * @return true if status update succeeds
     */
    public boolean updateAppointmentStatus(int appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentDao.getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Cannot update status: Appointment not found.");
        }

        AppointmentStatus currentStatus = appointment.getStatus();

        if (currentStatus == AppointmentStatus.CANCELLED &&
                status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("A CANCELLED appointment cannot become COMPLETED.");
        }

        if (currentStatus == AppointmentStatus.COMPLETED &&
                status == AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("A COMPLETED appointment cannot go back to SCHEDULED.");
        }

        appointment.setStatus(status);
        appointmentDao.updateAppointment(appointment);

        return true;
    }

    /**
     * Updates patient information.
     *
     * @param patient updated patient
     */
    public void updatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        if (patientDao.getPatientById(patient.getPatientId()) == null) {
            throw new IllegalArgumentException("Cannot update: Patient does not exist.");
        }

        // Validates activity status of a patient in the system.
        Patient existing = patientDao.getPatientById(patient.getPatientId());
        if (existing == null || !existing.isActive()) {
            throw new IllegalArgumentException("Cannot update: Patient does not exist or is inactive.");
        }

        patientDao.updatePatient(patient);
    }

    /**
     * Deletes a patient only if they have no active scheduled appointments.
     *
     * @param patientId patient ID
     */
    public void deletePatient(int patientId) {
        if (patientDao.getPatientById(patientId) == null) {
            throw new IllegalArgumentException("Cannot delete: Patient does not exist.");
        }

        List<Appointment> appointments = appointmentDao.getAppointmentsByPatient(patientId);

        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                throw new IllegalStateException("Cannot delete patient with active appointments.");
            }
        }

        patientDao.deletePatient(patientId);
    }

    /**
     * Updates provider information.
     *
     * @param provider updated provider
     */
    public void updateProvider(Provider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null.");
        }

        if (providerDao.getProviderById(provider.getProviderId()) == null) {
            throw new IllegalArgumentException("Cannot update: Provider does not exist.");
        }

        // Validates activity status of a provider in the system.
        Provider existing = providerDao.getProviderById(provider.getProviderId());
        if (existing == null || !existing.isActive()) {
            throw new IllegalArgumentException("Cannot update: Provider does not exist or is inactive.");
        }

        providerDao.updateProvider(provider);
    }

    /**
     * Deletes a provider only if they have no active scheduled appointments.
     *
     * @param providerId provider ID
     */
    public void deleteProvider(int providerId) {
        if (providerDao.getProviderById(providerId) == null) {
            throw new IllegalArgumentException("Cannot delete: Provider does not exist.");
        }

        List<Appointment> appointments = appointmentDao.getAppointmentsByProvider(providerId);

        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                throw new IllegalStateException("Cannot delete provider with active appointments.");
            }
        }

        providerDao.deleteProvider(providerId);
    }

    /**
     * Gets appointments by patient ID.
     *
     * @param patientId patient ID
     * @return matching appointments
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentDao.getAppointmentsByPatient(patientId);
    }

    /**
     * Gets appointments by provider ID.
     *
     * @param providerId provider ID
     * @return matching appointments
     */
    public List<Appointment> getAppointmentsByProvider(int providerId) {
        return appointmentDao.getAppointmentsByProvider(providerId);
    }

    /**
     * Gets appointments within a date range.
     *
     * @param startDate start date
     * @param endDate end date
     * @return appointments in date range
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return appointmentDao.getAppointmentsByDateRange(startDate, endDate);
    }

    /**
     * Gets appointments by appointment status.
     *
     * @param status appointment status
     * @return matching appointments
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentDao.getAppointmentsByStatus(status);
    }

    /**
     * Validates that appointment start time is before end time
     * and that the appointment is not scheduled in the past.
     *
     * @param startTime start time
     * @param endTime end time
     */
    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Invalid time range: start time must be before end time.");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointments cannot be made in the past.");
        }
    }

    /**
     * Checks whether a provider has a conflicting appointment.
     *
     * @param providerId provider ID
     * @param startTime proposed start time
     * @param endTime proposed end time
     * @param appointmentIdToIgnore appointment ID to ignore during rescheduling
     * @return true if a conflict exists
     */
    private boolean hasProviderConflict(int providerId,
                                        LocalDateTime startTime,
                                        LocalDateTime endTime,
                                        int appointmentIdToIgnore) {

        List<Appointment> existingAppointments =
                appointmentDao.getAppointmentsByProvider(providerId);

        for (Appointment existing : existingAppointments) {
            if (existing.getAppointmentId() == appointmentIdToIgnore) {
                continue;
            }

            if (existing.getStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }

            boolean overlaps =
                    startTime.isBefore(existing.getEndDateTime()) &&
                            endTime.isAfter(existing.getStartDateTime());

            if (overlaps) {
                return true;
            }
        }

        return false;
    }
}