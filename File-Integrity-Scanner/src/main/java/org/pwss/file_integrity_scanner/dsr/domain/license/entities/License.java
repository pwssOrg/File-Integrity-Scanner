package org.pwss.file_integrity_scanner.dsr.domain.license.entities;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Entity representing a License in the application.
 *
 * <p>
 * This entity maps to the "license" table in the database. It contains
 * information about
 * individual licenses, including their unique IDs and license data.
 * </p>
 */
@Entity(name = "license")
public class License extends PWSSbaseEntity {

    /**
     * Returns the section of the application this entity belongs to.
     *
     * <p>
     * In this case, it returns {@link #LICENSE}, indicating that this entity
     * is related to the license functionality within the application.
     * </p>
     *
     * @return The string "License" representing the section of the application this
     *         entity
     *         belongs to.
     */
    @Override
    protected String getDBSection() {
        return LICENSE;
    }

    /**
     * The unique identifier for this license.
     *
     * <p>
     * This field maps to the "id" column in the database. It is marked as
     * {@code @Id} indicating it's the primary key, and cannot be null or updated
     * after
     * creation.
     * </p>
     */
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Integer id;

    /**
     * The data content of this license.
     *
     * <p>
     * This field maps to the "license_data" column in the database. It is marked as
     * non-nullable and cannot be updated after creation. Each value must be unique
     * across all
     * rows.
     * </p>
     */
    @Column(name = "license_data", nullable = false, unique = true, updatable = false)
    private String licenseData;

    /**
     * Returns the unique identifier for this license.
     *
     * <p>
     * This is a getter method for accessing the value of the {@code id} field,
     * which maps to the "id" column in the database table.
     * </p>
     *
     * @return The ID of the license as an Integer.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the data content of this license.
     *
     * <p>
     * This is a getter method for accessing the value of the {@code licenseData}
     * field,
     * which maps to the "license_data" column in the database table.
     * </p>
     *
     * @return The license data as a String.
     */
    public String getLicenseData() {
        return licenseData;
    }
}
