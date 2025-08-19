package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Checksum} entities.
 *
 * This interface extends {@link JpaRepository}, providing CRUD operations and
 * additional custom query methods
 * to interact with the database table that stores checksum information.
 *
 * @author PWSS ORG
 */
@Repository
public interface ChecksumRepository extends JpaRepository<Checksum, Long> {
    /**
     * Finds all checksums associated with a given file.
     *
     * This method searches for {@link Checksum} entities related to the specified
     * {@link File}
     * and returns them as an {@link Optional} containing a {@link List}.
     *
     * @param file The {@link File} entity used to filter checksums. Must not be
     *             null.
     * @return An {@link Optional} containing a list of {@link Checksum} entities
     *         associated with the given file,
     *         or an empty {@link Optional} if no matching checksums are found.
     */
    Optional<List<Checksum>> findByFile(File file);
}
