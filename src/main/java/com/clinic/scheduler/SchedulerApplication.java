package com.clinic.scheduler;

import com.clinic.scheduler.model.Appointment;
import com.clinic.scheduler.model.AppointmentStatus;
import com.clinic.scheduler.model.Patient;
import com.clinic.scheduler.model.Provider;
import com.clinic.scheduler.service.AppointmentManager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Serves as the main Spring Boot entry point and command-line user interface
 * for the clinic appointment scheduler.
 */
@SpringBootApplication
public class SchedulerApplication implements CommandLineRunner {

	private final AppointmentManager appointmentManager;
	private final Scanner scanner = new Scanner(System.in);

	/**
	 * Constructs the application with the AppointmentManager service.
	 *
	 * @param appointmentManager the service layer that handles business logic
	 */
	public SchedulerApplication(AppointmentManager appointmentManager) {
		this.appointmentManager = appointmentManager;
	}

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

	/**
	 * Runs the CLI menu after Spring Boot starts.
	 *
	 * @param args command-line arguments
	 */
	@Override
	public void run(String... args) {
		boolean running = true;

		while (running) {
			printMenu();

			try {
				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
					case 1 -> addPatient();
					case 2 -> addProvider();
					case 3 -> scheduleAppointment();
					case 4 -> viewAppointmentsByPatient();
					case 5 -> viewAppointmentsByProvider();
					case 6 -> viewAppointmentsByDateRange();
					case 7 -> viewAppointmentsByStatus();
					case 8 -> rescheduleAppointment();
					case 9 -> updateAppointmentStatus();
					case 10 -> updatePatient();
					case 11 -> updateProvider();
					case 12 -> deletePatient();
					case 13 -> deleteProvider();
					case 0 -> {
						System.out.println("Exiting Clinic Appointment Scheduler.");
						running = false;
					}
					default -> System.out.println("Invalid choice. Please try again.");
				}

			} catch (NumberFormatException e) {
				System.out.println("Input error: Please enter a valid number.");
			} catch (IllegalArgumentException e) {
				System.out.println("Input error: " + e.getMessage());
			} catch (IllegalStateException e) {
				System.out.println("Operation error: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}

	/**
	 * Prints the main CLI menu.
	 */
	private void printMenu() {
		System.out.println();
		System.out.println("Clinic Appointment Scheduler");
		System.out.println("1. Add Patient");
		System.out.println("2. Add Provider");
		System.out.println("3. Schedule Appointment");
		System.out.println("4. View Appointments by Patient");
		System.out.println("5. View Appointments by Provider");
		System.out.println("6. View Appointments by Date Range");
		System.out.println("7. View Appointments by Status");
		System.out.println("8. Reschedule Appointment");
		System.out.println("9. Update Appointment Status");
		System.out.println("10. Update Patient");
		System.out.println("11. Update Provider");
		System.out.println("12. Delete Patient");
		System.out.println("13. Delete Provider");
		System.out.println("0. Exit");
		System.out.print("Choice: ");
	}

	/**
	 * Adds a new patient.
	 */
	private void addPatient() {
		// Read and parse Patient's ID
		int id = readInt("Patient ID");

		// Read and collect Patient's name
		System.out.print("Name: ");
		String name = scanner.nextLine();

		// Read and parse Patient's Date of Birth
		LocalDate dateOfBirth = readLocalDate("Date of Birth YYYY-MM-DD: ");

		// Read and collect Patient's contact info
		System.out.print("Contact Info: ");
		String contactInfo = scanner.nextLine();

		// Instantiate Patient, add to database.
		Patient patient = new Patient(id, name, dateOfBirth, contactInfo);
		appointmentManager.addPatient(patient);

		System.out.println("Patient added successfully.");
	}

	/**
	 * Adds a new provider.
	 */
	private void addProvider() {
		int id = readInt("Provider ID");

		System.out.print("Name: ");
		String name = scanner.nextLine();

		System.out.print("Specialty: ");
		String specialty = scanner.nextLine();

		System.out.print("Location: ");
		String location = scanner.nextLine();

		Provider provider = new Provider(id, name, specialty, location);
		appointmentManager.addProvider(provider);

		System.out.println("Provider added successfully.");
	}

	/**
	 * Schedules a new appointment.
	 */
	private void scheduleAppointment() {
		// Read and parse Patient and Provider's ID
		int patientId = readInt("Patient ID");
		int providerId = readInt("Provider ID");

		// Read and collect reason
		System.out.print("Reason: ");
		String reason = scanner.nextLine();

		// Read and parse Start/End Time
		LocalDateTime start = readLocalDateTime("Start Date/Time YYYY-MM-DDTHH:MM");
		LocalDateTime end = readLocalDateTime("End Date/Time YYYY-MM-DDTHH:MM");

		// Instantiate appointment object, save to database
		Appointment appointment = appointmentManager.scheduleAppointment(
				patientId,
				providerId,
				reason,
				start,
				end
		);

		System.out.println("Appointment scheduled successfully:");
		System.out.println(appointment);
	}

	/**
	 * Retrieves appointments for a specific patient.
	 */
	private void viewAppointmentsByPatient() {
		int patientId = readInt("Patient ID");

		List<Appointment> appointments =
				appointmentManager.getAppointmentsByPatient(patientId);

		printAppointments(appointments);
	}

	/**
	 * Retrieves appointments for a specific provider.
	 */
	private void viewAppointmentsByProvider() {
		int providerId = readInt("Provider ID");

		List<Appointment> appointments =
				appointmentManager.getAppointmentsByProvider(providerId);

		printAppointments(appointments);
	}

	/**
	 * Retrieves appointments within a date range.
	 */
	private void viewAppointmentsByDateRange() {
		LocalDate startDate = readLocalDate("Start Date YYYY-MM-DD");
		LocalDate endDate = readLocalDate("End Date YYYY-MM-DD");

		List<Appointment> appointments =
				appointmentManager.getAppointmentsByDateRange(startDate, endDate);

		printAppointments(appointments);
	}

	/**
	 * Retrieves appointments by appointment status.
	 */
	private void viewAppointmentsByStatus() {
		System.out.print("Status SCHEDULED, COMPLETED, or CANCELLED: ");
		AppointmentStatus status =
				AppointmentStatus.valueOf(scanner.nextLine().toUpperCase());

		List<Appointment> appointments =
				appointmentManager.getAppointmentsByStatus(status);

		printAppointments(appointments);
	}

	/**
	 * Reschedules an existing appointment.
	 */
	private void rescheduleAppointment() {
		int appointmentId = readInt("Appointment ID");

		// Read and parse new start and end times.
		LocalDateTime start = readLocalDateTime("New Start Date/Time YYYY-MM-DDTHH:MM");
		LocalDateTime end = readLocalDateTime("New End Date/Time YYYY-MM-DDTHH:MM");

		appointmentManager.rescheduleAppointment(appointmentId, start, end);

		System.out.println("Appointment rescheduled successfully.");
	}

	/**
	 * Updates an appointment status.
	 */
	private void updateAppointmentStatus() {
		int appointmentId = readInt("Appointment ID");

		System.out.print("Status SCHEDULED, COMPLETED, or CANCELLED: ");
		AppointmentStatus status =
				AppointmentStatus.valueOf(scanner.nextLine().toUpperCase());

		appointmentManager.updateAppointmentStatus(appointmentId, status);

		System.out.println("Appointment status updated successfully.");
	}

	/**
	 * Updates an existing patient.
	 */
	private void updatePatient() {
		int id = readInt("Patient ID");

		System.out.print("Updated Name: ");
		String name = scanner.nextLine();

		LocalDate dateOfBirth = readLocalDate("Updated Date of Birth YYYY-MM-DD");

		System.out.print("Updated Contact Info: ");
		String contactInfo = scanner.nextLine();

		Patient patient = new Patient(id, name, dateOfBirth, contactInfo);
		appointmentManager.updatePatient(patient);

		System.out.println("Patient updated successfully.");
	}

	/**
	 * Updates an existing provider.
	 */
	private void updateProvider() {
		int id = readInt("Provider ID");

		System.out.print("Updated Name: ");
		String name = scanner.nextLine();

		System.out.print("Updated Specialty: ");
		String specialty = scanner.nextLine();

		System.out.print("Updated Location: ");
		String location = scanner.nextLine();

		Provider provider = new Provider(id, name, specialty, location);
		appointmentManager.updateProvider(provider);

		System.out.println("Provider updated successfully.");
	}

	/**
	 * Deletes a patient if allowed by business rules.
	 */
	private void deletePatient() {
		int patientId = readInt("Patient ID");

		appointmentManager.deletePatient(patientId);

		System.out.println("Patient deleted successfully.");
	}

	/**
	 * Deletes a provider if allowed by business rules.
	 */
	private void deleteProvider() {
		int providerId = readInt("Provider ID");

		appointmentManager.deleteProvider(providerId);

		System.out.println("Provider deleted successfully.");
	}

	/**
	 * Prints a list of appointments.
	 *
	 * @param appointments the appointments to print
	 */
	private void printAppointments(List<Appointment> appointments) {
		if (appointments == null || appointments.isEmpty()) {
			System.out.println("No appointments found.");
			return;
		}

		System.out.println("\n=== Appointments ===");

		for (Appointment a : appointments) {
			System.out.println("------------------------------");
			System.out.println("Appointment ID : " + a.getAppointmentId());
			System.out.println("Patient        : " + a.getPatient().getName()
					+ " (ID: " + a.getPatient().getPatientId() + "), active: " + a.getPatient().isActive());
			System.out.println("Provider       : " + a.getProvider().getName()
					+ " (" + a.getProvider().getSpecialty() + "), active: " + a.getProvider().isActive() );
			System.out.println("Location       : " + a.getProvider().getLocation());
			System.out.println("Start Time     : " + a.getStartDateTime());
			System.out.println("End Time       : " + a.getEndDateTime());
			System.out.println("Status         : " + a.getStatus());
			System.out.println("Reason         : " + a.getReason());
			System.out.println("------------------------------");
		}
	}

    /**
	 * Prompts the user for a date until a valid format is entered.
     * @param prompt that is printed to the user
     * @return the parsed LocalDate object
     */
	private LocalDate readLocalDate(String prompt) {
		while (true) {
			System.out.print(prompt + " (YYYY-MM-DD): ");
			try {
				return LocalDate.parse(scanner.nextLine());
			} catch (DateTimeParseException e) {
				System.out.println(" Invalid format. Use YYYY-MM-DD.");
			}
		}
	}

    /**
	 * Prompts the user for a date and time until a valid format is entered.
     * @param prompt that is printer to the user
     * @return the parsed LocalDateTime object
     */
	private LocalDateTime readLocalDateTime(String prompt) {
		while (true) {
			System.out.print(prompt + " (YYYY-MM-DDTHH:MM): ");
			try {
				return LocalDateTime.parse(scanner.nextLine());
			} catch (DateTimeParseException e) {
				System.out.println(" Invalid format. Use YYYY-MM-DDTHH:MM.");
			}
		}
	}

    /**
	 * Prompts the user for an integer until a valid number is entered.
     * @param prompt that is printed to the user
     * @return the parsed Integer object
     */
	private int readInt(String prompt) {
		while (true) {
			System.out.print(prompt + ": ");
			try {
				return Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println(" Invalid format. Please enter a numeric ID.");
			}
		}
	}
}