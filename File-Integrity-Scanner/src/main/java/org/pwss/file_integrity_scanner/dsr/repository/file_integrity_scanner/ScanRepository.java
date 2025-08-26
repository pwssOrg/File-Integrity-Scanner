package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing {@link Scan} entities.
 *
 * This interface extends {@link JpaRepository}, providing standard CRUD
 * operations
 * to interact with the database table that stores scan information.
 */
public interface ScanRepository extends JpaRepository<Scan, Integer> {

    /**
     * JPQL query to find the most recent scan by selecting the scan with the
     * maximum ID. For more information, see {@link <a href=
     * "https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html">docs.spring.io</a>}
     */
    final String findMostRecentScanQuery = "SELECT s FROM Scan s WHERE s.id = (SELECT MAX(s1.id) from Scan s1)";

    /**
     * Retrieves the most recent scan from the database.
     *
     * @return an {@code Optional} containing the most recent {@link Scan} if found,
     *         or empty if no scans are available
     */
    @Query(findMostRecentScanQuery)
    Optional<Scan> findMostRecentScan();

}
