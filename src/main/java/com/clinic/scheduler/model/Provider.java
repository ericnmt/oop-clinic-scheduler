package com.clinic.scheduler.model;

/**
 * Class to represent a provider/person that has an appointment with a patient.
 */
public class Provider {
    /**
     * Unique ID of the provider.
     */
    private int providerId;
    /**
     * Name of the provider.
     */
    private String name;
    /**
     * Provider's medical specialty.
     */
    private String specialty;
    /**
     * Location of provider.
     */
    private String location;

    /**
     * Construct a new provider in the system.
     *
     * @param providerId of the Provider
     * @param name of the Provider
     * @param specialty of the Provider
     * @param location of the Provider
     */
    public Provider(int providerId, String name, String specialty, String location) {
        this.providerId = providerId;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    // Getters and Setters
    /**
     * Get the ID of the provider.
     *
     * @return the provider's ID
     */
    public int getProviderId() {
        return providerId;
    }

    /**
     * Set the ID of a provider.
     * providerId should only be set ONCE when a provider is added to the system.
     *
     * @param providerId of the provider
     */
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    /**
     * Get the name of a provider.
     *
     * @return the provider's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of a provider.
     *
     * @param name of the provider
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the specialty of a provider.
     *
     * @return provider's medical specialty
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Set the specialty of a provider.
     *
     * @param specialty of the provider
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * Get the location of a provider.
     *
     * @return the location of the provider
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location of a provider.
     *
     * @param location of the provider
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Compose the provider's information into a readable string format.
     *
     * @return string of provider's details
     */
    // Provider toString method
    @Override
    public String toString() {
        return  "Name: " + name +
                " (ID " + providerId + ")" +
                ", specialty: " + specialty +
                ", location: " + location;
    }
}
