import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppoinmentManager {
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
    }

    /**
     * Method to handle the modification of appointment status
     * @param appointment is the Appointment that will be rescheduled
     * @param startTime is the start time of the new Appointment
     * @param endTime is the end time of the new Appointment
     * @return Appointment
     */
    public Appointment rescheduleAppointment(Appointment appointment, LocalDateTime startTime, LocalDateTime endTime) {
    }

    /**
     * Method to handle the rescheduling of appointments
     * Method to handle the
     * @param appointment is the Appointment's status that will be changed
     * @param status is the new status of the Appointment
     * @return boolean
     */
    public boolean updateAppointmentStatus(Appointment appointment, AppointmentStatus status) {
    }

    /**
     * Method to retrieve all Appointments by Patient
     * @param patient is the filter for the list of Appointments
     * @return Appointment
     */
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
    }

    /**
     * Method to retrieve all Appointments by Provider
     * @param provider is the filter for the list of Appointments
     * @return List of Appointments
     */
    public List<Appointment> getAppointmentsByProvider(Provider provider) {
    }

    /**
     * Method to retrieve all Appointments by a given date range
     * <p>
     *
     * </p>
     * @param startDate is the startDate of the range
     * @param endDate is the endDate of the range
     * @return List of Appointments
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
    }

    /**
     * Method to retrieve all Appointments by Status
     * <p>
     *
     * </p>
     * @param status is the filter for the list of Appointments
     * @return List of Appointments
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
    }
}