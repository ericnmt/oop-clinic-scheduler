package com.clinic.scheduler.dao;

import com.clinic.scheduler.model.Appointment;
import com.clinic.scheduler.model.AppointmentStatus;
import com.clinic.scheduler.model.Patient;
import com.clinic.scheduler.model.Provider;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface to define Appointment database CRUD operations.
 */
public interface AppointmentDao {
    // Create
    void createAppointment(Appointment appointment);

    // Read
    Appointment getAppointmentById(int id);
    List<Appointment> getAppointmentsByPatient(int patientId);
    List<Appointment> getAppointmentsByProvider(int providerId);
    List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate);
    List<Appointment> getAppointmentsByStatus(AppointmentStatus status);

    // Update
    void updateAppointment(Appointment appointment);

    // Delete
    void deleteAppointment(int id);
}


