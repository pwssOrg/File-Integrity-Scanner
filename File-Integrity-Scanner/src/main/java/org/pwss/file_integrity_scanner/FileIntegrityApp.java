package org.pwss.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.service.scan.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class FileIntegrityApp {
    public static void main(String[] args) {
        SpringApplication.run(FileIntegrityApp.class, args);
    }

    /**
     * For testing purposes, you can use the following SQL to insert a sample monitored directory
     * <p>
     * INSERT INTO monitored_directory (
     * path,
     * is_active,
     * added_at,
     * last_scanned,
     * notes,
     * baseline_established
     * ) VALUES (
     * 'YOUR PATH HERE',
     * TRUE,
     * NOW(),
     * NOW(),
     * 'Sample entry for testing',
     * FALSE
     * );
     */
    // For testing purposes, this component will trigger a scan of all directories
    @Component
    public class StartupScanner implements CommandLineRunner {

        private final ScanService scanService;

        @Autowired
        public StartupScanner(ScanService scanService) {
            this.scanService = scanService;
        }

        @Override
        public void run(String... args) {
            scanService.scanAllDirectories();
        }
    }
}