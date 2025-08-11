package org.pwss.file_integrity_scanner.msr.service.scan;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.pwss.file_integrity_scanner.dsr.service.scan.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanServiceTest {

    @Autowired
    ScanService scanService;

    @Test
    public void testUserServiceInitialization() {
        assertNotNull(scanService, "ScanService should not be null");
    }

}
