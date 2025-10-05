package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.NoActiveMonitoredDirectoriesException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.ScanAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanServiceTest {

    private ScanService scanServiceMock;

    private MonitoredDirectory monitoredDirectory;

    @Autowired
    private ScanService scanServiceSpy;

    @BeforeEach
    void setUp() {
        // Create a mock implementation of ScanService
        scanServiceMock = mock(ScanServiceImpl.class);

        // Create a test instance of MonitoredDirectory
        monitoredDirectory = new MonitoredDirectory();

        // A 'spy' differs from a 'mock' in that a spy is a real instance of the class,
        // while a mock is a simulated object. With a spy, you can call real methods and
        // partially stub behavior.

        scanServiceSpy = spy(scanServiceSpy);
    }

    @Test
    void testScanServiceInitialization() {
        assertNotNull(scanServiceMock, "ScanService should not be null");
    }

    @Test
    void testScanSingleDirectoryWithValidMonitoredDirectory() throws ScanAlreadyRunningException,NoActiveMonitoredDirectoriesException {
        // Call the method with a valid MonitoredDirectory object
        scanServiceMock.scanSingleDirectory(monitoredDirectory);
        // Verify that scanSingleDirectory was called with the correct argument
        verify(scanServiceMock).scanSingleDirectory(monitoredDirectory);
    }

    // Assert that a NullPointerException is thrown when scanSingleDirectory is
    // called with a null parameter.
    // We also provide a custom error message to display if no exception is thrown
    // (which would indicate a failure)
    @Test
    void testScanSingleDirectoryWithNullValue() {

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> ((ScanService) scanServiceSpy).scanSingleDirectory(null),
                "Expected scanSingleDirectory to throw, but it didn't");

        // Print the caught exception message for debugging or logging purposes
        System.out.println("Caught expected exception: " + exception.getMessage());

        // Verify that the thrown exception's message contains a specific string,
        // ensuring that the exception is meaningful and properly formatted.
        assertTrue(exception.getMessage().contains("Monitored directory cannot be null"),
                "Exception message should indicate null value for argument");

    }

}
