package org.pwss.file_integrity_scanner.msr.service.file;

import org.pwss.file_integrity_scanner.msr.repository.FileRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends BaseService<FileRepository> implements FileService {

  
  
  public void sendNotification(String message) throws InterruptedException {
    // Thead.sleep could be your async call to io_file/FileTraverser
      // Simulating a delay

      int i = 0;
    while(i < 900000000 ){
    i++;
    }

     System.out.println("Notification sent: " + message);

  }

    public FileServiceImpl(FileRepository repository){
    super(repository);
}
    
}
