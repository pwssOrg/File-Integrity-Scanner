package org.pwss.file_integrity_scanner.dsr.service.file;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file.FileService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileServiceTest {

    @Autowired 
    FileService fileService;

    @Autowired 
    FileServiceImpl fileServiceImpl;

    @Test
    void testUserServiceInitialization() {
    assertNotNull(fileService, "FileService should not be null");
    }
}
