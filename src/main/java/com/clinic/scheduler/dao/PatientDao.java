package com.clinic.scheduler.dao;

import com.clinic.scheduler.model.Patient;
import java.util.List;

/**
 * Interface to define Patient database CRUD operations.
 */
public interface PatientDao {
    // Create
    void createPatient(Patient patient);

    // Read
    Patient getPatientById(int id);
    List<Patient> getAllPatients();

    // Update
    void updatePatient(Patient patient);

    // Delete
    void deletePatient(int id);
}
