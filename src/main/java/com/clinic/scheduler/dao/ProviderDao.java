package com.clinic.scheduler.dao;

import com.clinic.scheduler.model.Provider;
import java.util.List;

/**
 * Interface to define Provider database CRUD operations.
 */
public interface ProviderDao {
    // Create
    void createProvider(Provider provider);

    // Read
    Provider getProviderById(int id);
    List<Provider> getAllProviders();

    // Update
    void updateProvider(Provider provider);

    // Delete
    void deleteProvider(int id);
}

