package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.file_integrity_controller;

/**
 * A record representing a response containing information about the scan status
 * and current live feed data.
 *
 * @param isScanRunning {@code true} if a scan is currently running;
 *                      {@code false} otherwise.
 * @param livefeed      The string representation of the current live feed data.
 */
public record LiveFeedResponse(boolean isScanRunning, String livefeed) {

}