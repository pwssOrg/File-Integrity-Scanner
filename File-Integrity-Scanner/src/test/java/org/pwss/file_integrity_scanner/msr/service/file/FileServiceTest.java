package org.pwss.file_integrity_scanner.msr.service.file;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;

@SpringBootTest
public class FileServiceTest {

    @Autowired 
    FileService fileService;

    @Autowired 
    FileServiceImpl fileServiceImpl;

    @Autowired
    TaskExecutor taskExecutor;

    @Test
    void testUserServiceInitialization() {
    assertNotNull(fileService, "FileService should not be null");
    }

    @Test
    void testAsync() throws InterruptedException, ExecutionException{


         // Start time before method invocation
        long startTime = System.currentTimeMillis();
         System.out.println("Start Time: "+ startTime);


     
       fileServiceImpl.sendNotification("hi");
         System.out.println("If this is printed before the notification - Async Works and a thread was created");

        

       
        // End time after method invocation
        long endTime = System.currentTimeMillis();
        System.out.println("End Time: "+endTime);

    }
    
}
