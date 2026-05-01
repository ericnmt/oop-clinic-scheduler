package com.clinic.scheduler.model;

import java.time.LocalDateTime;

/**
 * Class to represent the Appointment object between a patient and provider.
 */
public class Appointment {
    /**
     * Unique ID of the appointment.
     */
    private int appointmentId;
    /**
     * Patient associated with the appointment.
     */
    private Patient patient;
    /**
     * Provider associated with the appointment.
     */
    private Provider provider;
    /**
     * Start time of the appointment.
     */
    private LocalDateTime startDateTime;
    /**
     * End time of the appointment.
     */
    private LocalDateTime endDateTime;
    /**
     * ENUM status of the appointment.
     */
    private AppointmentStatus status;
    /**
     * Reason for the appointment.
     */
    private String reason;

    /**
     * Constructor for the Appointment object.
     *
     * @param appointmentId of Appointment, unique
     * @param patient who will be seen
     * @param provider who will see patient
     * @param startDateTime of Appointment
     * @param endDateTime of Appointment
     * @param reason of Appointment
     * AppointmentStatus is set to SCHEDULED by default until changed
     */
    public Appointment(int appointmentId, Patient patient, Provider provider, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.provider = provider;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = AppointmentStatus.SCHEDULED;
        this.reason = reason;
    }

    // Getters & Setters
    /**
     * Get an appointment's unique ID.
     *
     * @return the appointment's ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Set an appointment's unique ID.
     *
     * @param appointmentId of the appointment
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Get the Patient object of an Appointment.
     *
     * @return the Patient of the appointment
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Set the patient who's associated with a Appointment.
     *
     * @param patient associated with the appointment
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Get the Provider object of an Appointment.
     *
     * @return the Provider of the appointment
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Set the provider who's associated with a Appointment.
     *
     * @param provider associated with the appointment
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    /**
     * Get the start date of a Appointment.
     *
     * @return the start date of the Appointment
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Set the start time of a Appointment.
     * Must be before endDateTime
     * Must be after current date time
     *
     * @param startDateTime of the appointment
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Get the start date of a Appointment.
     *
     * @return the start time of the appointment
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Set the end time of a Appointment.
     *
     * @param endDateTime of the appointment
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Get the status of a Appointment.
     *
     * @return the status of the appointment
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Set the status of a Appointment.
     *
     * @param status of the appointment
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Get the reason for the scheduling of a Appointment.
     *
     * @return the reason for the appointment
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set the reason for the scheduling of a Appointment.
     *
     * @param reason for the appointment
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    // Methods
    /**
     * Update the status of an existing Appointment.
     *
     * @param newStatus of the appointment
     * @return true if the update was successful
     */

    public boolean updateStatus(AppointmentStatus newStatus) {
        this.status = newStatus;
        return true;
    }

    /**
     * Reschedule an existing Appointment to a new time.
     *
     * @param newStart time of the appointment
     * @param newEnd time of the appointment
     * @return true if the appointment was successfully rescheduled
     */
    public boolean reschedule(LocalDateTime newStart, LocalDateTime newEnd) {
        this.startDateTime = newStart;
        this.endDateTime = newEnd;
        return true;
    }

    /**
     * Returns a Appointment's information as a readable string.
     *
     * @return appointment details as a string
     */
    @Override
    public String toString() {
        return  "------------------------------\n" +
                "NEW APPOINTMENT CREATED\n" +
                "------------------------------\n" +
                "Appointment ID : " + appointmentId + "\n" +
                "Patient        : " + patient + "\n" +
                "Provider       : " + provider + "\n" +
                "Start Time     : " + startDateTime + "\n" +
                "End Time       : " + endDateTime + "\n" +
                "Status         : " + status + "\n" +
                "Reason         : " + reason + "\n" +
                "------------------------------";
    }
}