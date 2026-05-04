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
public class ProviderDaoImpl implements ProviderDao {

    // Inject JdbcTemplate
    private final JdbcTemplate jdbcTemplate;

    public ProviderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Maps DB row → Provider object
     */
    private final RowMapper<Provider> providerRowMapper = (rs, rowNum) -> {
        int providerId = rs.getInt("ProviderID");
        String name = rs.getString("Name");
        String specialty = rs.getString("Specialty");
        String location = rs.getString("Location");

        Provider provider = new Provider(providerId, name, specialty, location);
        provider.setActive(rs.getInt("IsActive") == 1);
        return provider;
    };

    /**
     * Create provider
     */
    @Override
    public void createProvider(Provider provider) {
        String sql = "INSERT INTO Provider (ProviderID, Name, Specialty, Location, isActive) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                provider.getProviderId(),
                provider.getName(),
                provider.getSpecialty(),
                provider.getLocation(),
                provider.isActive() ? 1: 0
        );
    }

    /**
     * Get provider by ID (FIXED: no crash if not found)
     */
    @Override
    public Provider getProviderById(int providerId) {
        String sql = "SELECT * FROM Provider WHERE ProviderID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, providerRowMapper, providerId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null; // important fix
        }
    }

    /**
     * Get all providers
     */
    @Override
    public List<Provider> getAllProviders() {
        String sql = "SELECT * FROM Provider WHERE IsActive = 1";
        return jdbcTemplate.query(sql, providerRowMapper);
    }

    /**
     * Update provider (FIXED SQL)
     */
    @Override
    public void updateProvider(Provider provider) {
        String sql = "UPDATE Provider SET Name = ?, Specialty = ?, Location = ? WHERE ProviderID = ?";
        jdbcTemplate.update(sql,
                provider.getName(),
                provider.getSpecialty(),
                provider.getLocation(),
                provider.getProviderId()
        );
    }

    /**
     * Delete provider
     */
    @Override
    public void deleteProvider(int providerId) {
        String sql = "UPDATE Provider SET IsActive = 0 WHERE ProviderID = ?";
        jdbcTemplate.update(sql, providerId);
    }
}