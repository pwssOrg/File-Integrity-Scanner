package org.pwss.file_integrity_scanner.msr.service.monitored_directory;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.repository.MonitoredDirectoryRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link MonitoredDirectoryService} interface.
 */
@Service
public class MonitoredDirectoryServiceImpl extends BaseService<MonitoredDirectoryRepository> implements MonitoredDirectoryService {

    private final org.slf4j.Logger log;

    public MonitoredDirectoryServiceImpl(MonitoredDirectoryRepository repository) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(MonitoredDirectoryServiceImpl.class);
    }

    @Override
    public List<MonitoredDirectory> findByIsActive(boolean isActive) {
        Optional<List<MonitoredDirectory>> mOptional = repository.findByIsActive(isActive);

        if (mOptional.isPresent()) {
            return mOptional.get();
        } else {
            log.info("No monitored directories found with active status: {}", isActive);
            return null;
        }

    }

}