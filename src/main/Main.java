package main;
import main.java.com.clinic.scheduler.service.AppointmentManager;
import main.java.com.clinic.scheduler.model.*;
//import dao.Database;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    /**
     * main.Main method to demonstrate code functionality.
     *
     * @param args from stdin
     */
    public static void main(String[] args) {
        System.out.println("Checkpoint 1.5: Demo main.java.com.clinic.scheduler.model.Appointment Scheduling");

        AppointmentManager manager = new AppointmentManager();

        System.out.println("TEST 1: Valid entry creation");
        // Create patients (2) and provider objects
        Patient patient1 = new Patient(101, "John Doe", LocalDate.of(2000,01,01), "johndoe@example.com");
        Patient patient2 = new Patient(102, "Jane Doe", LocalDate.of(1999,12,31), "jane.doe@example.com");
        Provider provider1 = new Provider(701, "Dr. Smith", "Cardiology", "Room A-11");

        // Add patients and provider to main.java.com.clinic.scheduler.service.AppointmentManager map
        manager.addPatient(patient1);
        manager.addPatient(patient2);
        manager.addProvider(provider1);
        System.out.println("Successfully added (2) patients and provider to the system.");

        System.out.println("TEST 2: Valid scheduling");
        // start: 9 AM, end: 10 AM on April 10, 2026
        LocalDateTime validStart = LocalDateTime.of(2026, 4, 10, 9, 0);
        LocalDateTime validEnd = LocalDateTime.of(2026, 4, 10, 10, 0);

        Appointment appt1 = manager.scheduleAppointment(101, 701, "Routine checkup", validStart, validEnd);
        System.out.println("Successfully scheduled main.java.com.clinic.scheduler.model.Appointment ID: " + appt1.getAppointmentId() + " for patient ID 101");

        System.out.println("TEST 3: Valid status update");
        boolean updated = manager.updateAppointmentStatus(appt1.getAppointmentId(), AppointmentStatus.CANCELLED);
        if (updated) {
            System.out.println("Successfully updated main.java.com.clinic.scheduler.model.Appointment ID: " + appt1.getAppointmentId() + " to " + appt1.getStatus());
        }

        // Set appt1 status back to SCHEDULED for later use
        manager.updateAppointmentStatus(appt1.getAppointmentId(), AppointmentStatus.SCHEDULED);

        System.out.println("TEST 4: Invalid cases");
        // Case A: Invalid time range
        try {
            System.out.println("Case A: end time BEFORE start time");
            manager.scheduleAppointment(102, 701, "Consultation", validEnd, validStart);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
        // Case B: main.java.com.clinic.scheduler.model.Provider overlap
        try {
            System.out.println("Case B: provider overlap");
            // start: 9:35 AM, end: 10:30 AM on April 10, 2026
            LocalDateTime overlapStart = LocalDateTime.of(2026, 4, 10, 9, 30);
            LocalDateTime overlapEnd = LocalDateTime.of(2026, 4, 10, 10, 30);
            manager.scheduleAppointment(102, 701, "Consultation follow up", overlapStart, overlapEnd);
        } catch (IllegalStateException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
        // Case C: Missing Entity
        try {
            System.out.println("Case C: Invalid patient ID");
            manager.scheduleAppointment(999, 701, "Consultation", validStart, validEnd);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("TEST 5: Search methods");
        manager.scheduleAppointment(101, 701, "Follow-up", validStart.plusDays(2), validEnd.plusDays(2));

        List<Appointment> johnDoeAppts = manager.getAppointmentsByPatient(101);
        System.out.println("Retrieved " + johnDoeAppts.size() + " appointments for John Doe (ID 101)");
    }
}