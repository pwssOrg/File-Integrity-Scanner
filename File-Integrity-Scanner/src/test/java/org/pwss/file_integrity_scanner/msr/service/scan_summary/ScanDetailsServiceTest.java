package org.pwss.file_integrity_scanner.msr.service.scan_summary;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.pwss.file_integrity_scanner.dsr.service.scan_summary.ScanSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanDetailsServiceTest {

    @Autowired
    ScanSummaryService ScanDetailsService;

    @Test
    public void testUserServiceInitialization() {
        assertNotNull(ScanDetailsService, "ScanDetailsService should not be null");
    }

}