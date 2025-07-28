package org.pwss.file_integrity_scanner.msr.service.scan;

import jakarta.transaction.Transactional;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;
import org.pwss.file_integrity_scanner.msr.service.monitored_directory.MonitoredDirectoryService;
import org.pwss.file_integrity_scanner.msr.repository.ScanRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.pwss.file_integrity_scanner.msr.service.scan.component.DirectoryTraverser;
import org.pwss.file_integrity_scanner.msr.service.scan.component.FileHashComputer;
import org.pwss.file_integrity_scanner.msr.service.scan.component.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//NOTES: This is some initial progress and it is not complete.
// we need to manage scan statuses properly, I will catch more issues later.
@Service
public class ScanServiceImpl extends BaseService<ScanRepository> implements ScanService {

    private boolean isScanning = false; // This needs to be removed. If you use this threading strategy in another project you need
    // To use the volatile keyword. That keyword removes the caching from that boolean which is essintial when a boolean is used as a thread mutex.

    
    @Autowired
    private final MonitoredDirectoryService monitoredDirectoryService;

    @Autowired
    private DirectoryTraverser directoryTraverser;

    
    private final FileProcessor fileProcessor;

  
    
   // It is not recommended to use extra threading in Spring. It will bug Spring. Removing the threading below from this class is essential.
   // We can schedule a meeting tommorow  to discuss this. 
   // Threading in this class is not a good idea!
   


   
    // I am thankful for your hard work and determined spirit that inspires me. 
    // Occasionally, we must reassess our solutions to ensure their correct construction.
    // I have set my alarm for 16:00 tomorrow. Would it be possible to have a conversation about how to construct this code?
    //  (without using extra threads)
    




    // Thread to handle the scan process
    private Thread scanThread;
    // Using a fixed thread pool to handle parallel scans (maybe we dont want to do this but let's try)
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    public ScanServiceImpl(ScanRepository repository,
    MonitoredDirectoryService monitoredDirectoryService,
    FileProcessor fileProcessor) {
        super(repository);
        this.monitoredDirectoryService = monitoredDirectoryService;

        // This is better. @Autowired is less secure than constructor calls every day in the week.
        this.fileProcessor = new FileProcessor(new FileHashComputer());
    
    }

    @Override
    public void scanAllDirectories() {
        if (isScanning) {
            System.out.println("Scan already in progress :D");
            return;
        }

        isScanning = true;

        scanThread = new Thread(() -> {
            try {
                List<MonitoredDirectory> directories = monitoredDirectoryService.findByIsActive(true);

                if (directories.isEmpty()) {
                    System.out.println("No active directories to scan :(");
                    isScanning = false;
                    return;
                }

                // Create a list to hold futures for each directory scan
                List<Future<?>> futures = new ArrayList<>();

                // Iterate over each monitored directory in database
                for (MonitoredDirectory dir : directories) {
                    // Run each directory scan in parallel
                    futures.add(executor.submit(() -> {
                        Scan scan = new Scan();
                        scan.setMonitoredDirectory(dir);
                        scan.setScanTime(OffsetDateTime.now());
                        scan.setStatus("IN_PROGRESS");
                        repository.save(scan);

                        scanDirectory(dir, scan);

                        scan.setStatus("COMPLETED");
                        repository.save(scan);
                    }));
                }

                // Wait for all scans to complete
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        // maybe we want to handle this somehow for each individual scan
                        System.out.println("Scan interrupted");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            } finally {
                isScanning = false;
            }
        });

        // Start the scan thread
        scanThread.start();
    }

    @Override
    @Transactional
    public void scanDirectory(MonitoredDirectory monitoredDirectory, Scan scanInstance) {
        try {
            List<Path> paths = directoryTraverser.scanDirectory(monitoredDirectory.getPath());

            for (Path path : paths) {
                if (!isScanning) {
                    System.out.println("Scan stopped prematurely :o");
                    break;
                }
                fileProcessor.process(path, scanInstance);
            }

        } catch (Exception e) {
            e.printStackTrace();
            scanInstance.setStatus("FAILED");
            repository.save(scanInstance);
        }
    }

    @Override
    public void stopScan() {
        if (isScanning) {
            isScanning = false;
            if (scanThread != null && scanThread.isAlive()) {
                scanThread.interrupt();
            }
            executor.shutdownNow();
        }
    }
}
