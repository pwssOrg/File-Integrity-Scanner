
package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

/**
 * The {@code DiffCountRequest} class represents a request to count differences
 * in a specific scan.
 *
 * This record encapsulates the ID of the scan for which the difference count is
 * requested. It serves as
 * a data carrier object that provides a simple and immutable way to pass the
 * scan ID parameter to methods
 * that perform the actual counting operation.
 *
 * @param scanId The unique identifier of the scan for which differences should
 *               be counted.
 */
public record DiffCountRequest(Integer scanId) {
}
