package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.CreateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.UpdateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.directory_controller.CreateMonitoredDirectoryResponse;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.MonitoredDirectoryRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link MonitoredDirectoryService} interface.
 */
@Service
public class MonitoredDirectoryServiceImpl
        extends PWSSbaseService<MonitoredDirectoryRepository, MonitoredDirectory, Integer>
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
            return new ArrayList<MonitoredDirectory>();
        }

    }

    @Override
    public void save(MonitoredDirectory mDirectory) {
        repository.save(mDirectory);
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

    @Override
    public CreateMonitoredDirectoryResponse createMonitoredDirectoryFromRequest(
            CreateMonitoredDirectoryRequest createRequest) throws SecurityException, NullPointerException {

        if (validateRequest(createRequest)) {

            try {
                Optional<MonitoredDirectory> oMonitoredDirectory = Optional
                        .of(repository.save(new MonitoredDirectory(createRequest.path(), createRequest.isActive(),
                                createRequest.includeSubdirectories(), OffsetDateTime.now())));

                if (oMonitoredDirectory.isPresent()) {
                    MonitoredDirectory monitoredDirectory = oMonitoredDirectory.get();
                    return new CreateMonitoredDirectoryResponse(monitoredDirectory);

                } else {
                    log.error("NullPointerException");
                    throw new NullPointerException("Monitored directory cannot be null");
                }
            }

            catch (Exception exception) {
                log.error("Error when creating a new monitored directory {}", exception);
                throw exception;
            }
        }

        else {
            log.error("SecurityException");
            throw new SecurityException("Could not validate the input request object");
        }

    }

    @Override
    public Optional<List<MonitoredDirectory>> findAll() {
        return Optional.of(repository.findAll());
    }

    @Transactional
    @Override
    public Boolean updateMonitoredDirectoryFromRequest(UpdateMonitoredDirectoryRequest request) throws SecurityException {

        if (validateRequest(request)) {

            Optional<MonitoredDirectory> mOptional = repository.findById(request.id());

            if (mOptional.isPresent()) {

                MonitoredDirectory mDirectory = mOptional.get();

                mDirectory.setNotes(request.notes());

                mDirectory.setIncludeSubdirectories(request.includeSubDirs());
                mDirectory.setIsActive(request.isActive());

                repository.save(mDirectory);
                return true;
            } else {

                log.error("NullPointerException");
                return false;

            }

        }

        else {
            log.error("SecurityException");
            throw new SecurityException("Could not validate the input request object");
        }
    }
}