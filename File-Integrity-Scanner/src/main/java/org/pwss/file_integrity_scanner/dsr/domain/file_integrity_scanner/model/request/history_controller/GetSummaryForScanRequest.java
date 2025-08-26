package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller;

/**
 * A request object used to retrieve summaries associated with a specific scan.
 *
 * This record holds the ID of the scan whose summaries are being requested.
 */
public record GetSummaryForScanRequest(Integer scanId) {}
