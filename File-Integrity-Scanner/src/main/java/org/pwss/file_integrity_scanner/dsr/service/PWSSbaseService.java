package org.pwss.file_integrity_scanner.dsr.service;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Base service class providing common functionality for all services that
 * interact with repositories.
 *
 * This abstract class defines a generic repository-based service pattern which
 * can be extended
 * by concrete implementations to provide specific business logic while reusing
 * the common CRUD operations.
 *
 * @param <Repository> The type of JPA repository used by this service. Must
 *                     extend {@link JpaRepository}.
 * @param <T>          The entity type managed by the repository. Must extend
 *                     {@link PWSSbaseEntity}.
 * @param <ID>         The type of identifier for entities in the repository.
 *                     Must extend {@link Number}.
 * @author PWSS ORG
 */
public abstract class PWSSbaseService<Repository extends JpaRepository<T, ID>, T extends PWSSbaseEntity, ID extends Number> {

    /** The repository used by this service to perform database operations. */
    protected final Repository repository;

    /**
     * Constructs a new instance of the service with the specified repository.
     *
     * @param repository The repository that will be managed by this service.
     */
    @Autowired(required = false) // Optional if repository might not be injected in some cases
    protected PWSSbaseService(Repository repository) {
        this.repository = repository;
    }

    /**
     * Gets the maximum count of records in the table managed by this service.
     *
     * This method is a convenience wrapper around the repository's {@link JpaRepository#count()} method,
     * providing an easy way to get the total number of records managed by the repository.
     *
     * @return The total number of records in the table managed by this service.
     */
    public long getMaxCount() {
        return repository.count();
    }
}
