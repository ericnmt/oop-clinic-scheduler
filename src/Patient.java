import java.time.LocalDate;

public class Patient{
    private int patientId;
    private String name;
    private LocalDate dateOfBirth;
    private String contactInfo;

    /**
     * Constructor, all parameters must exist
     * @param patientId of Patient
     * @param name of Patient
     * @param dateOfBirth of Patient
     * @param contactInfo of Patient
     */
    public Patient(int patientId, String name, LocalDate dateOfBirth, String contactInfo) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    // REVISE: A patientId should always remain the same
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    // REVISE: A patient may have multiple methods of contact
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // Patient toString method
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
