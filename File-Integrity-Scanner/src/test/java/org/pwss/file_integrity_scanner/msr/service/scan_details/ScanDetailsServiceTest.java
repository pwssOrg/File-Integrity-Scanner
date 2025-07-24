package org.pwss.file_integrity_scanner.msr.service.scan_details;

import org.pwss.file_integrity_scanner.msr.service.scan_details.ScanDetailsService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanDetailsServiceTest {

    @Autowired
    ScanDetailsService ScanDetailsService;

    @Test
    public void testUserServiceInitialization() {
        assertNotNull(ScanDetailsService, "ScanDetailsService should not be null");
    }

}