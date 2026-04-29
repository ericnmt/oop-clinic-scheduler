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
 * Service layer class that handles business logic for patients,
 * providers, and appointments.
 *
 * This class enforces validation rules and delegates database
 * operations to DAO interfaces.
 */
@Service
public class AppointmentManager {

    private final PatientDao patientDao;
    private final ProviderDao providerDao;
    private final AppointmentDao appointmentDao;

    /**
     * Constructor for dependency injection.
     */
    public AppointmentManager(PatientDao patientDao,
                              ProviderDao providerDao,
                              AppointmentDao appointmentDao) {
        this.patientDao = patientDao;
        this.providerDao = providerDao;
        this.appointmentDao = appointmentDao;
    }

    /**
     * Adds a new patient.
     */
    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        patientDao.createPatient(patient);
    }

    /**
     * Adds a new provider.
     */
    public void addProvider(Provider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null.");
        }
        providerDao.createProvider(provider);
    }

    /**
     * Schedules a new appointment.
     */
    public void scheduleAppointment(int patientId,
                                    int providerId,
                                    String reason,
                                    LocalDateTime startTime,
                                    LocalDateTime endTime) {

        Patient patient = patientDao.getPatientById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient does not exist.");
        }

        Provider provider = providerDao.getProviderById(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("Provider does not exist.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start must be before end.");
        }

        // 🔥 conflict check (manual because DAO doesn't provide it)
        List<Appointment> providerAppointments =
                appointmentDao.getAppointmentsByProvider(providerId);

        for (Appointment existing : providerAppointments) {
            if (startTime.isBefore(existing.getEndDateTime()) &&
                    endTime.isAfter(existing.getStartDateTime())) {
                throw new IllegalStateException("Provider has conflicting appointment.");
            }
        }

        Appointment appointment = new Appointment(
                0, // ID handled by DB
                patient,
                provider,
                startTime,
                endTime,
                reason
        );

        appointmentDao.createAppointment(appointment);
    }

    /**
     * Reschedules an appointment.
     */
    public void rescheduleAppointment(int appointmentId,
                                      LocalDateTime startTime,
                                      LocalDateTime endTime) {

        Appointment appointment = appointmentDao.getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED ||
                appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot modify completed/cancelled appointment.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Invalid time range.");
        }

        int providerId = appointment.getProvider().getProviderId();

        List<Appointment> providerAppointments =
                appointmentDao.getAppointmentsByProvider(providerId);

        for (Appointment existing : providerAppointments) {
            if (existing.getAppointmentId() != appointmentId &&
                    startTime.isBefore(existing.getEndDateTime()) &&
                    endTime.isAfter(existing.getStartDateTime())) {
                throw new IllegalStateException("Provider conflict.");
            }
        }

        appointment.setStartDateTime(startTime);
        appointment.setEndDateTime(endTime);

        appointmentDao.updateAppointment(appointment);
    }

    /**
     * Updates appointment status.
     */
    public void updateAppointmentStatus(int appointmentId, AppointmentStatus status) {

        Appointment appointment = appointmentDao.getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }

        appointment.setStatus(status);
        appointmentDao.updateAppointment(appointment);
    }

    /**
     * Updates a patient.
     */
    public void updatePatient(Patient patient) {
        if (patientDao.getPatientById(patient.getPatientId()) == null) {
            throw new IllegalArgumentException("Patient does not exist.");
        }
        patientDao.updatePatient(patient);
    }

    /**
     * Deletes a patient if no active appointments exist.
     */
    public void deletePatient(int patientId) {

        List<Appointment> appointments =
                appointmentDao.getAppointmentsByPatient(patientId);

        for (Appointment appt : appointments) {
            if (appt.getStatus() == AppointmentStatus.SCHEDULED) {
                throw new IllegalStateException("Patient has active appointments.");
            }
        }

        patientDao.deletePatient(patientId);
    }

    /**
     * Updates a provider.
     */
    public void updateProvider(Provider provider) {
        if (providerDao.getProviderById(provider.getProviderId()) == null) {
            throw new IllegalArgumentException("Provider does not exist.");
        }
        providerDao.updateProvider(provider);
    }

    /**
     * Deletes a provider if no active appointments exist.
     */
    public void deleteProvider(int providerId) {

        List<Appointment> appointments =
                appointmentDao.getAppointmentsByProvider(providerId);

        for (Appointment appt : appointments) {
            if (appt.getStatus() == AppointmentStatus.SCHEDULED) {
                throw new IllegalStateException("Provider has active appointments.");
            }
        }

        providerDao.deleteProvider(providerId);
    }

    /**
     * Gets appointments by patient.
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentDao.getAppointmentsByPatient(patientId);
    }

    /**
     * Gets appointments by provider.
     */
    public List<Appointment> getAppointmentsByProvider(int providerId) {
        return appointmentDao.getAppointmentsByProvider(providerId);
    }

    /**
     * Gets appointments by date range.
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return appointmentDao.getAppointmentsByDateRange(startDate, endDate);
    }

    /**
     * Gets appointments by status.
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentDao.getAppointmentsByStatus(status);
    }
}