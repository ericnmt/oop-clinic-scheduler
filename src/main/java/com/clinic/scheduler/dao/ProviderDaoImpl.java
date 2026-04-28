package com.clinic.scheduler.dao;

import com.clinic.scheduler.model.Provider;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;


/**
 * Implementation of ProviderDao interface. Directly interacts with database through SQL queries.
 */
@Repository
public class ProviderDaoImpl implements ProviderDao{

    // Inject JDBCTemplate
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to inject jdbc template using the above definition.
     *
     * @param jdbcTemplate to inject
     */
    public ProviderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Method to map provider's instance fields to DB table columns (defined in schema.sql)
     */
    private final RowMapper<Provider> providerRowMapper = (rs, rowNum) -> {
        int providerId = rs.getInt("ProviderID");
        String name = rs.getString("Name");
        String specialty = rs.getString("Specialty");
        String location = rs.getString("Location");

        return new Provider(providerId, name, specialty, location);
    };

    /**
     * Create a provider, map it to the Database.
     *
     * @param provider to be created
     */
    @Override
    public void createProvider(Provider provider) {
        String sql = "INSERT INTO Provider (ProviderID, Name, Specialty, Location) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, provider.getProviderId(), provider.getName(), provider.getSpecialty(), provider.getLocation());
    }

    /**
     * Filter a Provider by their ID.
     *
     * @param providerId to search for
     * @return Provider object that matches providerId provided
     */
    @Override
    public Provider getProviderById(int providerId) {
        String sql = "SELECT * FROM Provider WHERE ProviderID = ?";
        return jdbcTemplate.queryForObject(sql, providerRowMapper, providerId);
    }

    /**
     * Get all Providers from the database.
     *
     * @return list of all Provider objects recorded.
     */
    @Override
    public List<Provider> getAllProviders() {
        String sql = "SELECT * FROM Provider";
        return jdbcTemplate.query(sql, providerRowMapper);
    }

    /**
     * Update a Provider's information.
     * Sorts through database by providerId.
     *
     * @param provider to update
     */
    @Override
    public void updateProvider(Provider provider) {
        String sql = "UPDATE Provider SET Name - ?, Specialty = ?, Location = ?, WHERE ProviderID = ?";
        jdbcTemplate.update(sql, provider.getName(), provider.getSpecialty(), provider.getLocation(), provider.getProviderId());
    }

    /**
     * Remove a provider form the database.
     *
     * @param providerId of provider to remove
     */
    @Override
    public void deleteProvider(int providerId) {
        String sql = "DELETE FROM Provider WHERE ProviderID = ?";
        jdbcTemplate.update(sql, providerId);
    }
}
