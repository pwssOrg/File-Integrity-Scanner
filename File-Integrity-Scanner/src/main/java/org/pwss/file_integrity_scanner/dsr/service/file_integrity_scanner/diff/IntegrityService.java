package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;

//TODO: Add Java Docs
public interface IntegrityService {

       /**
     * Persists a diff entity in the database.
     * <p>
     * This method saves or updates the provided {@code Diff}
     * entity, ensuring its state is recorded persistently. It handles all
     * necessary operations to make the entity available for future queries.
     *
     * @param entity the {@link Diff} entity to be saved or
     *                   updated in the database
     */
    void save(Diff entity);
    
}
