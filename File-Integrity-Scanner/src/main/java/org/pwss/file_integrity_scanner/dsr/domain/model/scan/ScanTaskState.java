package org.pwss.file_integrity_scanner.dsr.domain.model.scan;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import org.pwss.file_integrity_scanner.dsr.domain.entities.scan.Scan;

/**
 * A record representing the state of a scan task.
 */
public record ScanTaskState(
        /**
         * The future result that holds a list of files when the scan completes.
         */
        Future<List<File>> future,

        /**
         * The scan entity associated with this task.
         */
        Scan scan) {
}
