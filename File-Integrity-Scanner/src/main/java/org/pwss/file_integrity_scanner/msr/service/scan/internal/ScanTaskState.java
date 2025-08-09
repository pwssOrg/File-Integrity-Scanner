package org.pwss.file_integrity_scanner.msr.service.scan.internal;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

public record ScanTaskState(Future<List<File>> future, Scan scan) {
}
