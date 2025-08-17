package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoredDirectoryServiceTest {

    MonitoredDirectoryService monitoredDirectoryServiceMock;
    private MonitoredDirectory monitoredDirectoryMock;

    @Autowired
    MonitoredDirectoryService monitoredDirectoryServiceSpy;

    // Create a spy on the actual MonitoredDirectory to allow partial mocking.
    MonitoredDirectory monitoredDirectorySpy;

    @BeforeEach
     void setUp() {

        monitoredDirectoryMock = mock(MonitoredDirectory.class);
        monitoredDirectorySpy = spy(new MonitoredDirectory());

        monitoredDirectoryServiceMock = mock(MonitoredDirectoryService.class);
        monitoredDirectoryServiceSpy = spy(monitoredDirectoryServiceSpy);

        // Note on Mockito Spy: This allows for testing both actual implementation code
        // and overriding specific parts when needed.
    }

    @Test
     void testUserServiceInitialization() {
        assertNotNull(monitoredDirectoryServiceMock, "monitoredDirectoryService should not be null");
    }

    @Test
     void testIsBaseLineEstablished_ReturnsTrue_WhenBaselineIsTrue() {
        // Arrange
        when(monitoredDirectoryMock.getBaselineEstablished()).thenReturn(true);

        // Act
        Boolean result = monitoredDirectoryServiceSpy.isBaseLineEstablished(monitoredDirectoryMock);

        // Assert
        assertTrue(result);
    }

    @Test
     void testIsBaseLineEstablished_ReturnsFalse_WhenBaselineIsFalse() {
        // Arrange
        when(monitoredDirectoryMock.getBaselineEstablished()).thenReturn(false);

        // Act
        Boolean result = monitoredDirectoryServiceSpy.isBaseLineEstablished(monitoredDirectoryMock);

        // Assert
        assertFalse(result);
    }

    @Test
     void testGetBaselineEstablished_ReturnsTrue_WhenSetBaselineEstablishedToTrue() {
        // Arrange
        monitoredDirectorySpy.setBaselineEstablished(true);

        // Act & Assert
        assertTrue(monitoredDirectorySpy.getBaselineEstablished());
    }

}
