package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

/**
 * A record representing a request to reset the baseline for a monitored
 * directory.
 *
 * @param directoryId  The unique identifier of the directory. This ID is used
 *                     to locate
 *                     the specific directory for which the baseline needs to be
 *                     reset.
 * @param endpointCode The code associated with the endpoint that is making this
 *                     request.
 *                     This can be used for tracking or authorization purposes.
 */
public record ResetBaseLineRequest(Integer directoryId, Long endpointCode) {
}
