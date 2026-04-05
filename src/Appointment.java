import java.time.LocalDateTime;
public class Appointment {
    private int appointmentId;
    private Patient patient;
    private Provider provider;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private AppointmentStatus status;
    private String reason;

    /**
     * @param appointmentId of Appointment, unique
     * @param patient who will be seen
     * @param provider who will see patient
     * @param startDateTime of Appointment
     * @param endDateTime of Appointment
     * @param reason of Appointment
     * AppointmentStatus is set to SCHEDULED by default until changed
     */
    // Constructor, all parameters must exist
    public Appointment(int appointmentId, Patient patient, Provider provider, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.provider = provider;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = AppointmentStatus.SCHEDULED;
        this.reason = reason;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // Methods
    public boolean updateStatus(AppointmentStatus newStatus) {
        this.status = newStatus;
        return true;
    }

    // REVISE: Enforce business rules for scheduling logic
    public boolean reschedule(LocalDateTime newStart, LocalDateTime newEnd) {
        this.startDateTime = newStart;
        this.endDateTime = newEnd;
        return true;
    }
}