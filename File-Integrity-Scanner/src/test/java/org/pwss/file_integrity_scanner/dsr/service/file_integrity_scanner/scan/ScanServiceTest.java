package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.StartScanByIdRequest;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.NoActiveMonitoredDirectoriesException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.ScanAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScanServiceTest {

    private ScanService scanServiceMock;

    @Autowired
    private ScanService scanServiceSpy;

    @BeforeEach
    void setUp() {
        // Create a mock implementation of ScanService
        scanServiceMock = mock(ScanServiceImpl.class);

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
    void testScanSingleDirectoryWithValidMonitoredDirectory()
            throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException {

        StartScanByIdRequest startScanByIdRequest = new StartScanByIdRequest(1, 5000L);

        // Call the method with a valid startScanByIdRequest object
        scanServiceMock.scanSingleDirectory(startScanByIdRequest);
        // Verify that scanSingleDirectory was called with the correct argument
        verify(scanServiceMock).scanSingleDirectory(startScanByIdRequest);
    }

    /**
     * Test that verifies a SecurityException is thrown when scanSingleDirectory is
     * called with a StartScanByIdRequest
     * containing null parameters.
     *
     * This test ensures the service correctly handles invalid input by throwing an
     * appropriate exception,
     * which indicates validation failure. The custom error message provides clear
     * feedback in case no exception
     * is thrown, signifying a potential failure in the implementation.
     */
    @Test
    void testScanSingleDirectoryWithNullValue() {

        StartScanByIdRequest startScanByIdRequest = new StartScanByIdRequest(1, null);
        SecurityException exception = assertThrows(
                SecurityException.class,
                () -> ((ScanService) scanServiceSpy).scanSingleDirectory(startScanByIdRequest),
                "Expected scanSingleDirectory to throw, but it didn't");

        // Print the caught exception message for debugging or logging purposes
        System.out.println("Caught expected exception: " + exception.getMessage());

        // Verify that the thrown exception's message contains a specific string,
        // ensuring that the exception is meaningful and properly formatted.
        assertTrue(exception.getMessage().contains("Validation Failed"),
                "Exception message should indicate that the validation for the request failed");

    }

}
