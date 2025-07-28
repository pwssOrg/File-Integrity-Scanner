package org.pwss.file_integrity_scanner.msr.service.monitored_directory;

import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.repository.MonitoredDirectoryRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link MonitoredDirectoryService} interface.
 */
@Service
public class MonitoredDirectoryServiceImpl extends BaseService<MonitoredDirectoryRepository> implements MonitoredDirectoryService {

    public MonitoredDirectoryServiceImpl(MonitoredDirectoryRepository repository) {
        super(repository);
    }

    @Override
    public List<MonitoredDirectory> findByIsActive(boolean isActive) {
       
        // Optional lets you handle null errors 
        Optional<List<MonitoredDirectory>> mOptional = repository.findByIsActive(isActive);

        if(mOptional.isPresent()){
            return mOptional.get();
        }
        else{
            // Write a ERROR log message (you can add the same log dependecy as in PWSS File Nav)
            // I will create a ticket for it :) 
            return null; 
        }
        
    }

}