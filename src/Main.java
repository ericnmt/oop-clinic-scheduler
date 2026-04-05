import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Main method to test functionality of basic class code translations
public class Main {
    public static void main(String[] args) {
        System.out.println("Checkpoint 1.5: Demo Appointment Scheduling");

        AppointmentManager manager = new AppointmentManager();

        System.out.println("TEST 1: Valid entry creation");
        // Create patients (2) and provider objects
        Patient patient1 = new Patient(101, "John Doe", LocalDate.of(2000,01,01), "johndoe@example.com");
        Patient patient2 = new Patient(102, "Jane Doe", LocalDate.of(1999,12,31), "jane.doe@example.com");
        Provider provider1 = new Provider(701, "Dr. Smith", "Cardiology", "Room A-11");

        // Add patients and provider to AppointmentManager map
        manager.addPatient(patient1);
        manager.addPatient(patient2);
        manager.addProvider(provider1);
        System.out.println("Successfully added (2) patients and provider to the system.\n");

        System.out.println("TEST 2: Valid scheduling");
        // start: 9 AM, end: 10 AM on April 10, 2026
        LocalDateTime validStart = LocalDateTime.of(2026, 4, 10, 9, 0);
        LocalDateTime validEnd = LocalDateTime.of(2026, 4, 10, 10, 0);

        Appointment appt1 = manager.scheduleAppointment(101, 701, "Routine checkup", validStart, validEnd);
        System.out.println("Successfully scheduled Appointment ID: " + appt1.getAppointmentId() + " for patient ID 101");
    }
}
