import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentManager {

    /**
     * Storage initialization for Patient, Appointment, Provider objects
     * Patient and Provider stored as HashMap with the "id (int)" field being the key
     * Appointment objects stored in array (list)
     * nextAppointmentId is auto-incrementing
     */
    private Map<Integer, Patient> patientDirectory;
    private Map<Integer, Provider> providerDirectory;
    private List<Appointment> appointmentList;
    private int nextAppointmentId;

    /**
     * Constructor for AppointmentManager class
     */
    public AppointmentManager() {
        this.patientDirectory = new HashMap<>();
        this.providerDirectory = new HashMap<>();
        this.appointmentList = new ArrayList<>();
        this.nextAppointmentId = 1;
    }

    // Validation methods for adding a Patient and Provider
    /**
     * Logic: 1. check if null, 2. check if already exists,
     * 3. otherwise, add patient to directory map with their patientId as the key.
     *
     * @param patient to be validated
     */
    public void addPatient(Patient patient) {
        // Exception if patient object is null
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        // Exception if patient object already exists
        if (patientDirectory.containsKey(patient.getPatientId())) {
            throw new IllegalArgumentException("Patient ID already exists");
        }
        patientDirectory.put(patient.getPatientId(), patient);
    }

    // Logic methods
    /**
     * Method to handle scheduling of appointments
     * @param patient is the Patient that the Appointment will be created with
     * @param provider is the Provider that the Appointment will be created with
     * @param reason is the reason for the Appointment
     * @param startTime is the start of the Appointment
     * @param endTime is the end of the Appointment (Must be AFTEER startTime
     * @return Appointment
     */
    public Appointment scheduleAppointment(Patient patient, Provider provider, String reason, LocalDateTime startTime, LocalDateTime endTime) {
        // TBA: Scheduling logic: validate time, provider availability
        return null;
    }

    /**
     * Method to handle the modification of appointment status
     * @param appointment is the Appointment that will be rescheduled
     * @param startTime is the start time of the new Appointment
     * @param endTime is the end time of the new Appointment
     * @return Appointment
     */
    public Appointment rescheduleAppointment(Appointment appointment, LocalDateTime startTime, LocalDateTime endTime) {
        // TBA: Rescheduling logic: conflict checks, status validation
        return null;
    }

    /**
     * Method to handle the rescheduling of appointments
     * Method to handle the
     * @param appointment is the Appointment's status that will be changed
     * @param status is the new status of the Appointment
     * @return boolean, true if status is the same as new status
     */
    public boolean updateAppointmentStatus(Appointment appointment, AppointmentStatus status) {
        return false;
        // TBA: Validation for status changes
    }

    // Search Methods
    /**
     * Method to retrieve all Appointments by Patient
     * @param patient is the filter for the list of Appointments
     * @return Appointment
     */
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return new ArrayList<>();
        // TBA: Proper return signature, filter logic (by patientId)
    }

    /**
     * Method to retrieve all Appointments by Provider
     * @param provider is the filter for the list of Appointments
     * @return List of Appointments
     */
    public List<Appointment> getAppointmentsByProvider(Provider provider) {
        return new ArrayList<>();
        // TBA: Proper return signature, filter logic (by providerId)
    }

    /**
     * Method to retrieve all Appointments by a given date range
     * @param startDate is the startDate of the range
     * @param endDate is the endDate of the range
     * @return List of Appointments
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return new ArrayList<>();
        // TBA: Proper return signature, filter logic (by date range)
    }

    /**
     * Method to retrieve all Appointments by Status
     * @param status is the filter for the list of Appointments
     * @return List of Appointments
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return new ArrayList<>();
        // TBA: Proper return signature, filter logic (by status)
    }
}