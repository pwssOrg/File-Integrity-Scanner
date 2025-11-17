package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff;

import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.DiffCountRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.ScanIntegrityDiffRequest;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.ScanNotFoundException;

/**
 * Service interface for managing integrity diff entities.
 * This interface provides methods for persisting and retrieving {@link Diff}
 * entities,
 * as well as other operations related to integrity checks and scan results.
 */
public interface IntegrityService {

  /**
   * Persists a diff entity in the database.
   * <p>
   * This method saves or updates the provided {@code Diff}
   * entity, ensuring its state is recorded persistently. It handles all
   * necessary operations to make the entity available for future queries.
   *
   * @param entity the {@link Diff} entity to be saved or updated in the database
   */
  void save(Diff entity);

  /**
   * Retrieves a list of diff entities from the provided scan request.
   * <p>
   * This method queries the database based on the parameters specified in the
   * ScanIntegrityDiffRequest, and returns a list of matching Diff entities.
   *
   * @param request The {@link ScanIntegrityDiffRequest} containing criteria to
   *                filter diffs by scan.
   * @return A list of {@link Diff} entities that match the criteria specified in
   *         the request.
   * @throws SecurityException If there is an issue accessing the requested data
   *                           due to security restrictions.
   */
  List<Diff> retrieveDiffListFromScan(ScanIntegrityDiffRequest request) throws SecurityException;

  /**
   * Checks whether a given file is present in the diff history of the repository.
   *
   * @param file The file to check for presence in the diff history.
   * @return {@code true} if the file is found in the diff history, {@code false}
   *         otherwise.
   */
  boolean fileIsPresentInDiffHistory(File file);

  /**
   * Retrieves the count of differences from a scan based on the given request.
   *
   * @param request The DiffCountRequest object containing the details needed to
   *                identify the scan.
   * @return The count of differences as an Integer.
   * @throws SecurityException     If validation of the request fails.
   * @throws ScanNotFoundException If the specified scan is not found.
   */
  Integer retrieveDiffCountFromScan(DiffCountRequest request) throws SecurityException, ScanNotFoundException;
}
