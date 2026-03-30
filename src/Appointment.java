import java.time.LocalDateTime;
public class Appointment {
    private String appointmentId;
    private Patient patient;
    private Provider provider;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private AppointmentStatus status;
    private String reason;
}