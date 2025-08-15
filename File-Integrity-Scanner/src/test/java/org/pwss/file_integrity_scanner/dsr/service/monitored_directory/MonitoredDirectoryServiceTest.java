package org.pwss.file_integrity_scanner.dsr.service.monitored_directory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryService;
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
