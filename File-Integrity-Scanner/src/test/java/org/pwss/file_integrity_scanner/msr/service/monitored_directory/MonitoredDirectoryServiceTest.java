package org.pwss.file_integrity_scanner.msr.service.monitored_directory;

import org.pwss.file_integrity_scanner.msr.service.monitored_directory.MonitoredDirectoryService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoredDirectoryServiceTest {

    @Autowired
    MonitoredDirectoryService monitoredDirectoryService;

    @Test
    public void testUserServiceInitialization() {
        assertNotNull(monitoredDirectoryService, "monitoredDirectoryService should not be null");
    }

}
