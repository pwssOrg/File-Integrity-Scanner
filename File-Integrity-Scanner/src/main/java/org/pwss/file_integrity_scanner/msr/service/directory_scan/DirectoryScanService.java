package org.pwss.file_integrity_scanner.msr.service.directory_scan;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;


public interface DirectoryScanService {
    void scanAllDirectories();
    void scanDirectory(MonitoredDirectory monitoredDirectory, Scan scanInstance);
    void stopScan();
}
