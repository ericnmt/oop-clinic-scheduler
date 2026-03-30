public class Provider{
    private int providerId;
    private String name;
    private String specialty;
    private String location;

    // Constructor, all parameters must exist
    /**
     * @param providerId of Provider, unique
     * @param name of Provider
     * @param specialty of Provider
     * @param location of Provider
     */
    public Provider(int providerId, String name, String specialty, String location) {
        this.providerId = providerId;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    // Getters and Setters
    public int getProviderId() {
        return providerId;
    }

    // REVISE: A provider ID should never change after initially set
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Provider toString method
    @Override
    public String toString() {
        return "Provider{" +
                "providerId=" + providerId +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}