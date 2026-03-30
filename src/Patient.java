public class Patient{
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String contactInfo;

    public Patient(String patientID, String name, String dateOfBirth, String contactInfo) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public String getPatientID() {
        return patientID;
    }

    // REVISE: A patientID should always remain the same
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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
                "patientID='" + patientID + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
