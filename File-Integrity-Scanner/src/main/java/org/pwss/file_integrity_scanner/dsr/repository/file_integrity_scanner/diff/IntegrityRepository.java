package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.diff;

import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Diff} entities.
 * This interface provides methods to perform CRUD operations on Diff entities,
 * as well as custom query methods such as finding diffs by associated scan or
 * file.
 */
@Repository
public interface IntegrityRepository extends JpaRepository<Diff, Long> {

    /**
     * Finds a list of {@link Diff} entities by their associated scan.
     *
     * @param scan        The scan to filter the diffs by. If this parameter is
     *                    null,
     *                    all diffs will be returned regardless of their scan
     *                    association.
     * @param information Information about how to paginate and sort the results
     * @return A list of {@link Diff} entities that match the specified scan
     */
    List<Diff> findByIntegrityFail_Scan(Scan scan, Pageable information);

    /**
     * Finds a list of {@link Diff} entities by their associated file.
     *
     * @param file The file to filter the diffs by. If this parameter is null,
     *             an empty list will be returned.
     * @return A list of {@link Diff} entities that match the specified file
     */
    List<Diff> findByIntegrityFail_File(File file);

}
