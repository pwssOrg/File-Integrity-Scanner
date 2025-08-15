package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.MonitoredDirectoryRepository;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link MonitoredDirectoryService} interface.
 */
@Service
public class MonitoredDirectoryServiceImpl extends BaseService<MonitoredDirectoryRepository>
        implements MonitoredDirectoryService {

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
            log.warn("No monitored directories found with active status: {}", isActive);
            return null;
        }

    }

    @Override
    public void save(MonitoredDirectory mDirectory) {
        this.repository.save(mDirectory);
    }

    @Override
    public Boolean isBaseLineEstablished(MonitoredDirectory mDirectory) {
        return mDirectory.getBaselineEstablished();
    }

    @Override
    public Optional<MonitoredDirectory> findById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Boolean resetBaseline(MonitoredDirectory mDirectory) {

        try {

            mDirectory.setBaselineEstablished(false);

            repository.save(mDirectory);

            return true;
        }

        catch (Exception exception) {
            log.error("Exception occurred when trying to reset a baseline {}", exception.getMessage());
            return false;
        }
    }
}