package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanSummaryServiceTest {

    @Autowired
    ScanSummaryService scanSummaryService;

    @Test
     void testScanSummaryServiceInitialization() {
        assertNotNull(scanSummaryService, "ScanSummaryService should not be null");
    }

}