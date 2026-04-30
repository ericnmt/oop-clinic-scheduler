package com.clinic.scheduler;

import com.clinic.scheduler.dao.*;
import com.clinic.scheduler.model.*;
import com.clinic.scheduler.service.AppointmentManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Final Project Demo: DAO-backed Appointment Scheduling");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:clinic.db");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        ResourceDatabasePopulator populator =
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        PatientDao patientDao = new PatientDaoImpl(jdbcTemplate);
        ProviderDao providerDao = new ProviderDaoImpl(jdbcTemplate);
        AppointmentDao appointmentDao =
                new AppointmentDaoImpl(jdbcTemplate, patientDao, providerDao);

        AppointmentManager manager =
                new AppointmentManager(patientDao, providerDao, appointmentDao);

        System.out.println("TEST 1: Valid entry creation");

        Patient patient1 = new Patient(101, "John Doe",
                LocalDate.of(2000, 1, 1), "johndoe@example.com");

        Patient patient2 = new Patient(102, "Jane Doe",
                LocalDate.of(1999, 12, 31), "jane.doe@example.com");

        Provider provider1 = new Provider(701, "Dr. Smith",
                "Cardiology", "Room A-11");

        manager.addPatient(patient1);
        manager.addPatient(patient2);
        manager.addProvider(provider1);

        System.out.println("Successfully added patients and provider.");

        System.out.println("TEST 2: Valid scheduling");

        LocalDateTime validStart = LocalDateTime.of(2027, 4, 10, 9, 0);
        LocalDateTime validEnd = LocalDateTime.of(2027, 4, 10, 10, 0);

        Appointment appt1 = manager.scheduleAppointment(
                101, 701, "Routine checkup", validStart, validEnd
        );

        System.out.println("Successfully scheduled appointment.");

        System.out.println("TEST 3: Search appointments by patient");

        List<Appointment> johnDoeAppts = manager.getAppointmentsByPatient(101);
        System.out.println("Retrieved " + johnDoeAppts.size()
                + " appointments for John Doe.");

        System.out.println("TEST 4: Update appointment status");

        if (!johnDoeAppts.isEmpty()) {
            Appointment savedAppt = johnDoeAppts.get(0);

            manager.updateAppointmentStatus(savedAppt.getAppointmentId(), AppointmentStatus.CANCELLED);

            System.out.println("Updated appointment "
                    + savedAppt.getAppointmentId()
                    + " to CANCELLED.");
        } else {
            System.out.println("No appointments found to update.");
        }
        System.out.println("TEST 5: Invalid time range");

        try {
            manager.scheduleAppointment(102, 701,
                    "Consultation", validEnd, validStart);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("TEST 6: Invalid patient");

        try {
            manager.scheduleAppointment(999, 701,
                    "Consultation", validStart.plusDays(1), validEnd.plusDays(1));
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("TEST 7: Reschedule appointment");

        Appointment appt2 = manager.scheduleAppointment(
                102,
                701,
                "Follow-up",
                validStart.plusDays(2),
                validEnd.plusDays(2)
        );

        manager.rescheduleAppointment(
                appt2.getAppointmentId(),
                validStart.plusDays(3),
                validEnd.plusDays(3)
        );

        System.out.println("Rescheduled appointment "
                + appt2.getAppointmentId()
                + " successfully.");

        System.out.println("TEST 8: Update patient and provider");

        Patient updatedPatient = new Patient(101, "John Doe Updated",
                LocalDate.of(2000, 1, 1), "updatedjohn@example.com");

        Provider updatedProvider = new Provider(701, "Dr. Smith Updated",
                "Cardiology", "Room B-22");

        manager.updatePatient(updatedPatient);
        manager.updateProvider(updatedProvider);

        System.out.println("Updated patient and provider successfully.");

        System.out.println("TEST 9: Delete patient/provider constraints");

        try {
            manager.deletePatient(102);
        } catch (IllegalStateException e) {
            System.out.println("Expected delete patient error: " + e.getMessage());
        }

        try {
            manager.deleteProvider(701);
        } catch (IllegalStateException e) {
            System.out.println("Expected delete provider error: " + e.getMessage());
        }

        System.out.println("Demo complete.");
    }
}