package com.clinic.scheduler;

// ADDED IMPORTS
// Import AppointmentManager for business logic
// Import CommandLineRunner for Spring Boot DB interaction
import com.clinic.scheduler.service.AppointmentManager;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Serves as the main entry point (UI) for the user and the database.
 */
@SpringBootApplication
public class SchedulerApplication {

	// Inject Service Layer via the AppointmentManager constructor
	private final AppointmentManager appointmentManager;
	public SchedulerApplication(AppointmentManager appointmentManager) {
		this.appointmentManager = appointmentManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

	// Implement Scanner-while loop HERE:
}
