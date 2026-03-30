import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppoinmentManager {
    // Logic methods
    /**
     * Method to handle scheduling of appointments
     * <p>
     *
     * </p>
     * @param patient
     * @param provider
     * @param reason
     * @param startTime
     * @param endTime
     * @return Appointment
     */
    public Appointment scheduleAppointment(Patient patient, Provider provider, String reason, LocalDateTime startTime, LocalDateTime endTime) {
    }

    /**
     * Method to handle the modification of appointment status
     * <p>
     *
     * </p>
     * @param appointment
     * @param startTime
     * @param endTime
     * @return Appointment
     */
    public Appointment rescheduleAppointment(Appointment appointment, LocalDateTime startTime, LocalDateTime endTime) {
    }

    /**
     * Method to handle the rescheduling of appointments
     * <p>
     *
     * </p>
     * Method to handle the
     * @param appointment
     * @param status
     * @return boolean
     */
    public boolean updateAppointmentStatus(Appointment appointment, AppointmentStatus status) {
    }

    /**
     * Method to retrieve all Appointments by Patient
     * <p>
     *
     * </p>
     * @param patient
     * @return Appointment
     */
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
    }

    /**
     * Method to retrieve all Appointments by Provider
     * <p>
     *
     * </p>
     * @param provider
     * @return Appointment
     */
    public List<Appointment> getAppointmentsByProvider(Provider provider) {
    }

    /**
     * Method to retrieve all Appointments by a given date range
     * <p>
     *
     * </p>
     * @param startDate
     * @param endDate
     * @return Appointment
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
    }

    /**
     * Method to retrieve all Appointments by Status
     * <p>
     *
     * </p>
     * @param status
     * @return Appointment
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
    }
}