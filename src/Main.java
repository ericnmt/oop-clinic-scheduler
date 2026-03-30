import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Main method to test functionality of basic class code translations
public class Main {
    public static void main(String[] args) {
        System.out.println("Checkpoint 1: Demo Appointment Scheduling");

        // Initialize list for patients and providers
        List<Patient> patients = new ArrayList<>();
        List<Provider> providers = new ArrayList<>();
        // Initialize AppointmentManager
        AppointmentManager manager = new AppointmentManager();

        // Create patient
        Patient patient1 = new Patient(001, "John Doe", LocalDate.of(2000,01,01),"johndoe@example.com");
        // Add patient1 to list and print patient1 details
        patients.add(patient1);
        System.out.println("Registered Patient: " + patient1.toString());

        // Create provider
        Provider provider1 = new Provider(701, "Dr. Smith", "Cardiology", "Room A-11");
        providers.add(provider1);
        // Add provider1 to list and print provider1 details
        System.out.println("Registered Provider: " + provider1.toString());

        // Schedule appointment
        System.out.println("Appointment schedule demo:");
        // Schedule appointment on: April 1, 2026: 9AM to 9:30 AM
        // start and end times will be handled by the AppointmentManager
        LocalDateTime start = LocalDateTime.of(2026, 4, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 4, 1, 9, 30);

        // Create Appointment object, utilize AppointmentManager schedule method
        Appointment newAppointment = manager.scheduleAppointment(patient1, provider1, "Checkup", start, end);

        if (newAppointment != null) {
            System.out.println("New Appointment successfully scheduled for " + newAppointment.getPatient().getName() + " with provider " + newAppointment.getProvider().getName() + " at " + newAppointment.getStartDateTime());
        } else {
            System.out.println("Failed to create new appointment (Implementation TBA, scheduleAppointment() returned null)");
        }
    }
}
