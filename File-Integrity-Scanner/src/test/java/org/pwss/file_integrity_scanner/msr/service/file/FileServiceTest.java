package org.pwss.file_integrity_scanner.msr.service.file;

import org.pwss.file_integrity_scanner.msr.service.file.FileService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileServiceTest {

    @Autowired 
    FileService fileService;


    @Test
    public void testUserServiceInitialization() {
    assertNotNull(fileService, "FileService should not be null");
    }
    
}
