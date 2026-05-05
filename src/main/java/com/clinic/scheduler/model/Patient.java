package com.clinic.scheduler.model;

import java.time.LocalDate;

/**
 * Class to represent a patient/provider that requests to schedule an Appointment.
 */
public class Patient{
    /**
     * The unique ID of a patient.
     */
    private int patientId;
    /**
     * The name of the patient.
     */
    private String name;
    /**
     * The patient's date of birth.
     */
    private LocalDate dateOfBirth;
    /**
     * The patient's contact information.
     */
    private String contactInfo;

    /**
     * Construct a new patient in the system.
     *
     * @param patientId of the Patient
     * @param name of the Patient
     * @param dateOfBirth of the Patient
     * @param contactInfo of the Patient
     */
    public Patient(int patientId, String name, LocalDate dateOfBirth, String contactInfo) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    /**
     * Get the ID of a patient.
     *
     * @return the patient's ID
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Set the ID of a patient.
     * patientId should only be set ONCE when a patient is added to the system.
     *
     * @param patientId of the patient
     */
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    /**
     * Get the name of a patient.
     *
     * @return the patient's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of a patient.
     *
     * @param name of the patient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the date of birth of a patient.
     *
     * @return the patient's date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set the date of birth of a patient.
     *
     * @param dateOfBirth of the patient
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get the contact information of a patient.
     *
     * @return patient's contact information
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the contact information for a patient.
     *
     * @param contactInfo of the patient
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Compose a patient's information into a readable string format.
     *
     * @return patient's information
     */
    @Override
    public String toString() {
        return  "Name: " + name +
                " (ID " + patientId + ")" +
                ", DOB: " + dateOfBirth +
                ", contact info: " + contactInfo;
    }
}
