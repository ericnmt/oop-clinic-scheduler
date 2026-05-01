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
		System.out.print("Patient ID: ");
		int id = Integer.parseInt(scanner.nextLine());

		System.out.print("Name: ");
		String name = scanner.nextLine();

		System.out.print("Date of Birth YYYY-MM-DD: ");
		LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());

		System.out.print("Contact Info: ");
		String contactInfo = scanner.nextLine();

		Patient patient = new Patient(id, name, dateOfBirth, contactInfo);
		appointmentManager.addPatient(patient);

		System.out.println("Patient added successfully.");
	}

	/**
	 * Adds a new provider.
	 */
	private void addProvider() {
		System.out.print("Provider ID: ");
		int id = Integer.parseInt(scanner.nextLine());

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
		System.out.print("Patient ID: ");
		int patientId = Integer.parseInt(scanner.nextLine());

		System.out.print("Provider ID: ");
		int providerId = Integer.parseInt(scanner.nextLine());

		System.out.print("Reason: ");
		String reason = scanner.nextLine();

		System.out.print("Start Date/Time YYYY-MM-DDTHH:MM: ");
		LocalDateTime start = LocalDateTime.parse(scanner.nextLine());

		System.out.print("End Date/Time YYYY-MM-DDTHH:MM: ");
		LocalDateTime end = LocalDateTime.parse(scanner.nextLine());

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
		System.out.print("Patient ID: ");
		int patientId = Integer.parseInt(scanner.nextLine());

		List<Appointment> appointments =
				appointmentManager.getAppointmentsByPatient(patientId);

		printAppointments(appointments);
	}

	/**
	 * Retrieves appointments for a specific provider.
	 */
	private void viewAppointmentsByProvider() {
		System.out.print("Provider ID: ");
		int providerId = Integer.parseInt(scanner.nextLine());

		List<Appointment> appointments =
				appointmentManager.getAppointmentsByProvider(providerId);

		printAppointments(appointments);
	}

	/**
	 * Retrieves appointments within a date range.
	 */
	private void viewAppointmentsByDateRange() {
		System.out.print("Start Date YYYY-MM-DD: ");
		LocalDate startDate = LocalDate.parse(scanner.nextLine());

		System.out.print("End Date YYYY-MM-DD: ");
		LocalDate endDate = LocalDate.parse(scanner.nextLine());

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
		System.out.print("Appointment ID: ");
		int appointmentId = Integer.parseInt(scanner.nextLine());

		System.out.print("New Start Date/Time YYYY-MM-DDTHH:MM: ");
		LocalDateTime start = LocalDateTime.parse(scanner.nextLine());

		System.out.print("New End Date/Time YYYY-MM-DDTHH:MM: ");
		LocalDateTime end = LocalDateTime.parse(scanner.nextLine());

		appointmentManager.rescheduleAppointment(appointmentId, start, end);

		System.out.println("Appointment rescheduled successfully.");
	}

	/**
	 * Updates an appointment status.
	 */
	private void updateAppointmentStatus() {
		System.out.print("Appointment ID: ");
		int appointmentId = Integer.parseInt(scanner.nextLine());

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
		System.out.print("Patient ID: ");
		int id = Integer.parseInt(scanner.nextLine());

		System.out.print("Updated Name: ");
		String name = scanner.nextLine();

		System.out.print("Updated Date of Birth YYYY-MM-DD: ");
		LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());

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
		System.out.print("Provider ID: ");
		int id = Integer.parseInt(scanner.nextLine());

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
		System.out.print("Patient ID: ");
		int patientId = Integer.parseInt(scanner.nextLine());

		appointmentManager.deletePatient(patientId);

		System.out.println("Patient deleted successfully.");
	}

	/**
	 * Deletes a provider if allowed by business rules.
	 */
	private void deleteProvider() {
		System.out.print("Provider ID: ");
		int providerId = Integer.parseInt(scanner.nextLine());

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
					+ " (ID: " + a.getPatient().getPatientId() + ")");
			System.out.println("Provider       : " + a.getProvider().getName()
					+ " (" + a.getProvider().getSpecialty() + ")");
			System.out.println("Location       : " + a.getProvider().getLocation());
			System.out.println("Start Time     : " + a.getStartDateTime());
			System.out.println("End Time       : " + a.getEndDateTime());
			System.out.println("Status         : " + a.getStatus());
			System.out.println("Reason         : " + a.getReason());
			System.out.println("------------------------------");
		}
	}
}