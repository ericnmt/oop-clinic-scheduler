package com.clinic.scheduler.model;

/**
 * ENUMERATION class to represent the three status conditions a Appointment can have.
 */
public enum AppointmentStatus {
    /**
     * An appointment is scheduled.
     */
    SCHEDULED,
    /**
     * An appointment is completed.
     */
    COMPLETED,
    /**
     * An appointment is canceled.
     */
    CANCELLED
}
