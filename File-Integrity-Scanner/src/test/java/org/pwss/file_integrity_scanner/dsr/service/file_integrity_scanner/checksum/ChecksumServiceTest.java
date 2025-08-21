package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.checksum;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChecksumServiceTest {

    @Autowired
    ChecksumService checksumService;

    @Test
     void testChecksumServiceInitialization() {
        assertNotNull(checksumService, "ChecksumService should not be null");
    }

}
