package com.clinic.scheduler.service;

import com.clinic.scheduler.model.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.clinic.scheduler.model.Appointment;
import com.clinic.scheduler.model.AppointmentStatus;

import java.time.LocalDate;
import java.util.Map;

/**
 * Class to represent the core logic of appointment scheduling and modification.
 */
public class AppointmentManager {
    /**
     * hashMap dictionary for all Patients in memory.
     */
    private Map<Integer, Patient> patientDirectory;
    /**
     * hashMap dictionary for all Providers in memory.
     */
    private Map<Integer, Provider> providerDirectory;
    /**
     * List of all Appointments in memory.
     */
    private List<Appointment> appointmentList;
    /**
     * List of Appointments in memory.
     */
    private int nextAppointmentId;

    /**
     * Constructor for the main.java.com.clinic.scheduler.model.Appointment Manager.
     */
    public AppointmentManager() {
        this.patientDirectory = new HashMap<>();
        this.providerDirectory = new HashMap<>();
        this.appointmentList = new ArrayList<>();
        this.nextAppointmentId = 1;
    }

    // Validation methods for adding a main.java.com.clinic.scheduler.model.Patient and main.java.com.clinic.scheduler.model.Provider
    /**
     * Add a patient to the system.
     * Logic: 1. check if given arg is null, 2. check if already exists,
     * 3. otherwise, add patient to directory map with their patientId as the key.
     *
     * @param patient to be added to system
     */
    public void addPatient(Patient patient) {
        // Exception if patient object is null
        if (patient == null) {
            throw new IllegalArgumentException("main.java.com.clinic.scheduler.model.Patient cannot be null.");
        }
        // Exception if patient's patientId object already exists
        if (patientDirectory.containsKey(patient.getPatientId())) {
            throw new IllegalArgumentException("main.java.com.clinic.scheduler.model.Patient ID already exists.");
        }
        patientDirectory.put(patient.getPatientId(), patient);
    }

    /**
     * Add a provider to the system.
     * Uses the same logic to validate.
     *
     * @param provider to be added to system
     */
    public void addProvider(Provider provider) {
        // Exception if provider object is null
        if (provider == null) {
            throw new IllegalArgumentException("main.java.com.clinic.scheduler.model.Provider cannot be null.");
        }
        // Exception if provider's providerId already exists
        if (providerDirectory.containsKey(provider.getProviderId())) {
            throw new IllegalArgumentException("main.java.com.clinic.scheduler.model.Provider ID already exists.");
        }
        providerDirectory.put(provider.getProviderId(), provider);
    }

    // Logic methods
    /**
     * Primary method to handle the scheduling logic of a new main.java.com.clinic.scheduler.model.Appointment.
     *
     * @param patientId unique ID of the main.java.com.clinic.scheduler.model.Patient that the main.java.com.clinic.scheduler.model.Appointment will be created with
     * @param providerId unique ID of the main.java.com.clinic.scheduler.model.Provider that the main.java.com.clinic.scheduler.model.Appointment will be created with
     * @param reason for the main.java.com.clinic.scheduler.model.Appointment
     * @param startTime of the main.java.com.clinic.scheduler.model.Appointment (must be BEFORE endTime and AFTER current time)
     * @param endTime of the main.java.com.clinic.scheduler.model.Appointment
     * @return main.java.com.clinic.scheduler.model.Appointment object that was created if all conditions pass
     */
    public Appointment scheduleAppointment(int patientId, int providerId, String reason, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. main.java.com.clinic.scheduler.model.Patient and main.java.com.clinic.scheduler.model.Provider must exist, validate with patientId and providerId
        if (!patientDirectory.containsKey(patientId)) {
            throw new IllegalArgumentException("Cannot schedule: main.java.com.clinic.scheduler.model.Patient ID: " + patientId + " does not exist.");
        }
        if (!providerDirectory.containsKey(providerId)) {
            throw new IllegalArgumentException("Cannot schedule: main.java.com.clinic.scheduler.model.Provider ID: " + providerId + " does not exist.");
        }
        // 2. validate time (endTime > startTime) and startTime > current time
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Cannot schedule: Invalid time range: " + endTime + " must be after " + startTime);
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule: Appointments cannot be made in the past.");
        }

        Patient patient = patientDirectory.get(patientId);
        Provider provider = providerDirectory.get(providerId);
        // 3. No overlapping appointments for provider
        // Iterate through Appointments already scheduled
        for (Appointment existingAppointments : appointmentList) {
            // If an appointment already exists with a provider AND an overlapping appointment is NOT CANCELLED then validate time range
            if (existingAppointments.getProvider().getProviderId() == providerId && existingAppointments.getStatus() != AppointmentStatus.CANCELLED) {
                // If time range conflicts with given start and end times, throw exception
                if (startTime.isBefore(existingAppointments.getEndDateTime()) && endTime.isAfter(existingAppointments.getStartDateTime())) {
                    throw new IllegalStateException("Cannot schedule: main.java.com.clinic.scheduler.model.Provider has a conflicting appointment.");
                }
            }
        }
        // If all validations pass, create/save new appointment object, return appointment
        Appointment newAppt = new Appointment(nextAppointmentId++, patient, provider, startTime, endTime, reason);
        appointmentList.add(newAppt);
        return newAppt;
    }

    /**
     * Update the time that an main.java.com.clinic.scheduler.model.Appointment takes place.
     *
     * @param appointmentId of the main.java.com.clinic.scheduler.model.Appointment that will be rescheduled
     * @param startTime NEW start time of the main.java.com.clinic.scheduler.model.Appointment
     * @param endTime NEW end time of the main.java.com.clinic.scheduler.model.Appointment
     * @return main.java.com.clinic.scheduler.model.Appointment
     */
    public Appointment rescheduleAppointment(int appointmentId, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Search/find appointment in appointmentList
        Appointment apptToBeMoved = null;
        for (Appointment appt : appointmentList) {
            if (appt.getAppointmentId() == appointmentId) {
                apptToBeMoved = appt;
                break;
            }
        }

        // If appointment is not found, throw exception
        if (apptToBeMoved == null) {
            throw new IllegalArgumentException("Cannot Reschedule: main.java.com.clinic.scheduler.model.Appointment ID " + appointmentId + " not found.");
        }
        // 2. Check if appointment is allowed to be scheduled
        if (apptToBeMoved.getStatus() == AppointmentStatus.CANCELLED || apptToBeMoved.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reschedule an appointment that is Completed or Cancelled.");
        }
        // 3. Validate time
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Cannot reschedule: Start time must be strictly before the end time.");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule: Appointments cannot be moved to the past.");
        }
        // 4. Check for provider overlaps, ignore current (THIS) appt
        int providerId = apptToBeMoved.getProvider().getProviderId();
        for (Appointment existingAppt : appointmentList) {
            // Skipping current appointment being rescheduled
            if (existingAppt.getAppointmentId() == appointmentId) {
                continue;
            }
            // Validate provider with providerId AND if appointment is NOT cancelled, validate time range w/ other appointment
            if (existingAppt.getProvider().getProviderId() == providerId && existingAppt.getStatus() != AppointmentStatus.CANCELLED) {
                // Compare start and end times of appointment, throw exception if time range is violated.
                if (startTime.isBefore(existingAppt.getEndDateTime()) && endTime.isAfter(existingAppt.getStartDateTime())) {
                    throw new IllegalStateException("Cannot reschedule: main.java.com.clinic.scheduler.model.Provider has a conflicting appointment.");
                }
            }
        }
        // 5. Update times for appointment IF all checks are successful
        apptToBeMoved.setStartDateTime(startTime);
        apptToBeMoved.setEndDateTime(endTime);
        return apptToBeMoved;
    }

    /**
     * Handle the rescheduling logic of appointments.
     *
     * @param appointmentId of the main.java.com.clinic.scheduler.model.Appointment's status that will be updated
     * @param status NEW status of the main.java.com.clinic.scheduler.model.Appointment
     * @return true if the main.java.com.clinic.scheduler.model.Appointment's status was successfully updated
     */
    public boolean updateAppointmentStatus(int appointmentId, AppointmentStatus status) {
        // Search through list of appointment, get appointment that needs to be updated
        Appointment apptToBeUpdated = null;
        for (Appointment appt : appointmentList) {
            if (appt.getAppointmentId() == appointmentId) {
                apptToBeUpdated = appt;
                break;
            }
        }
        if (apptToBeUpdated == null) {
            throw new IllegalArgumentException("Cannot update status: main.java.com.clinic.scheduler.model.Appointment " + appointmentId + " not found.");
        }

        AppointmentStatus currentStatus = apptToBeUpdated.getStatus();

        // State transition logic, validate status limitations/rules
        // Method fails when: Status is being changed from CANCELLED to COMPLETED and
        // status is being changed from COMPLETED to SCHEDULED
        if (currentStatus == AppointmentStatus.CANCELLED && status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("A CANCELLED appointment cannot become COMPLETED.");
        }
        if (currentStatus == AppointmentStatus.COMPLETED && status == AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("A COMPLETED appointment cannot go back to SCHEDULED");
        }

        apptToBeUpdated.setStatus(status);
        return true;
    }

    // Search Methods
    /**
     * Retrieve all Appointments by main.java.com.clinic.scheduler.model.Patient ID.
     *
     * @param patientId is the filter for the list of Appointments
     * @return main.java.com.clinic.scheduler.model.Appointment(s) that match the provided patientId
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> results = new ArrayList<>();
        for (Appointment appt : appointmentList) {
            if (appt.getPatient().getPatientId() == patientId) {
                results.add(appt);
            }
        }
        return results;
    }

    /**
     * Retrieve all Appointments by main.java.com.clinic.scheduler.model.Provider ID.
     *
     * @param providerId (ID) is the filter for the list of Appointments
     * @return main.java.com.clinic.scheduler.model.Appointment(s) that match the provided providerId
     */
    public List<Appointment> getAppointmentsByProvider(int providerId) {
        List<Appointment> results = new ArrayList<>();
        for (Appointment appt : appointmentList) {
            if (appt.getProvider().getProviderId() == providerId) {
                results.add(appt);
            }
        }
        return results;
    }

    /**
     * Retrieve all Appointments by a given date range.
     *
     * @param startDate is the startDate of the range
     * @param endDate is the endDate of the range
     * @return main.java.com.clinic.scheduler.model.Appointment(s) that match the provided date range.
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Appointment> results = new ArrayList<>();
        for (Appointment appt : appointmentList) {
            LocalDate apptDate = appt.getStartDateTime().toLocalDate();
            // Check if apptDate is ON or AFTER startDate and ON or BEFORE endDate
            if (!apptDate.isBefore(startDate) && !apptDate.isAfter(endDate)) {
                results.add(appt);
            }
        }
        return results;
    }

    /**
     * Method to retrieve all Appointments by Status.
     *
     * @param status is the filter for the list of Appointments
     * @return main.java.com.clinic.scheduler.model.Appointment(s) that match the provided Status
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        List<Appointment> results = new ArrayList<>();
        for (Appointment appt : appointmentList) {
            if (appt.getStatus() == status) {
                results.add(appt);
            }
        }
        return results;
    }
}