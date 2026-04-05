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
        // Exception if patient's patientId object already exists
        if (patientDirectory.containsKey(patient.getPatientId())) {
            throw new IllegalArgumentException("Patient ID already exists.");
        }
        patientDirectory.put(patient.getPatientId(), patient);
    }

    /**
     * Same logic as addPatient method (above)
     *
     * @param provider to be validated
     */
    public void addProvider(Provider provider) {
        // Exception if provider object is null
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null.");
        }
        // Exception if provider's providerId already exists
        if (providerDirectory.containsKey(provider.getProviderId())) {
            throw new IllegalArgumentException("Provider ID already exists.");
        }
        providerDirectory.put(provider.getProviderId(), provider);
    }

    // Logic methods
    /**
     * Method to handle scheduling of appointments
     * @param patientId is the unique ID of the Patient that the Appointment will be created with
     * @param providerId is the unique ID of the Provider that the Appointment will be created with
     * @param reason is the reason for the Appointment
     * @param startTime is the start of the Appointment
     * @param endTime is the end of the Appointment (Must be AFTER startTime)
     * @return Appointment
     */
    public Appointment scheduleAppointment(int patientId, int providerId, String reason, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Patient and Provider must exist, validate with patientId and providerId
        if (!patientDirectory.containsKey(patientId)) {
            throw new IllegalArgumentException("Cannot schedule: Patient ID: " + patientId + " does not exist.");
        }
        if (!providerDirectory.containsKey(providerId)) {
            throw new IllegalArgumentException("Cannot schedule: Provider ID: " + providerId + " does not exist.");
        }
        // 2. validate time (endTime > startTime) and startTime > current time
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Cannot schedule: Invalid time range: " + endTime + " must be after " + startTime);
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule: Appointments cannot be made in the past.");
        }

        Patient patient = patientDirectory.get(patientId);
        Provider provider = providerDirectory.get(providerId);
        // 3. No overlapping appointments for provider
        // Iterate through Appointment already scheduled
        for (Appointment existingAppointments : appointmentList) {
            // If an appointment already exists with a provider AND an overlapping appointment is NOT CANCELLED then validate time range
            if (existingAppointments.getProvider().getProviderId() == providerId && existingAppointments.getStatus() != AppointmentStatus.CANCELLED) {
                // If time range conflicts with given start and end times, throw exception
                if (startTime.isBefore(existingAppointments.getEndDateTime()) && endTime.isAfter(existingAppointments.getStartDateTime())) {
                    throw new IllegalStateException("Cannot schedule: Provider has a conflicting appointment.");
                }
            }
        }
        // If all validations pass, create/save new appointment object, return appointment
        Appointment newAppt = new Appointment(nextAppointmentId++, patient, provider, startTime, endTime, reason);
        appointmentList.add(newAppt);
        return newAppt;
    }

    /**
     * Method to handle the modification of appointment status
     * @param appointmentId is the ID (key) of the Appointment that will be rescheduled
     * @param startTime is the start time of the new Appointment
     * @param endTime is the end time of the new Appointment
     * @return Appointment
     */
    public Appointment rescheduleAppointment(int appointmentId, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Search/find appointment in appointmentList
        Appointment apptToBeMoved = null;
        for (Appointment appt : appointmentList) {
            if (appt.getAppointmentId() == appointmentId) {
                apptToBeMoved = appt;
                break;
            }
        }

        // If appointment is not found, throw exception
        if (apptToBeMoved == null) {
            throw new IllegalArgumentException("Cannot Reschedule: Appointment ID " + appointmentId + " not found.");
        }
        // 2. Check if appointment is allowed to be scheduled
        if (apptToBeMoved.getStatus() == AppointmentStatus.CANCELLED || apptToBeMoved.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reschedule an appointment that is Completed or Cancelled.");
        }
        // 3. Validate time
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Cannot reschedule: Start time must be strictly before the end time.");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule: Appointments cannot be moved to the past.");
        }
        // 4. Check for provider overlaps, ignore current (THIS) appt
        int providerId = apptToBeMoved.getProvider().getProviderId();
        for (Appointment existingAppt : appointmentList) {
            // Skipping current appointment being rescheduled
            if (existingAppt.getAppointmentId() == appointmentId) {
                continue;
            }
            // Validate provider with providerId AND if appointment is NOT cancelled, validate time range w/ other appointment
            if (existingAppt.getProvider().getProviderId() == providerId && existingAppt.getStatus() != AppointmentStatus.CANCELLED) {
                // Compare start and end times of appointment, throw exception if time range is violated.
                if (startTime.isBefore(existingAppt.getEndDateTime()) && endTime.isAfter(existingAppt.getStartDateTime())) {
                    throw new IllegalStateException("Cannot reschedule: Provider has a conflicting appointment.");
                }
            }
        }
        // 5. Update times for appointment IF all checks are successful
        apptToBeMoved.setStartDateTime(startTime);
        apptToBeMoved.setEndDateTime(endTime);
        return apptToBeMoved;
    }

    /**
     * Method to handle the rescheduling of appointments
     * Method to handle the
     * @param appointmentId is the appointment ID where the status will be changed
     * @param status is the new status of the Appointment
     * @return boolean, true if status is the same as new status
     */
    public boolean updateAppointmentStatus(int appointmentId, AppointmentStatus status) {
        // Search through list of appointment, get appointment that needs to be updated
        Appointment apptToBeUpdated = null;
        for (Appointment appt : appointmentList) {
            if (appt.getAppointmentId() == appointmentId) {
                apptToBeUpdated = appt;
                break;
            }
        }
        if (apptToBeUpdated == null) {
            throw new IllegalArgumentException("Cannot update status: Appointment " + appointmentId + " not found.");
        }

        // State transition logic, validate status limitations/rules

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