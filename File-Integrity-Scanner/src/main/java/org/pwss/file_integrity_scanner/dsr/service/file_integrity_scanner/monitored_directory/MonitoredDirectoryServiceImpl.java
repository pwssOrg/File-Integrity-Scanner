package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.CreateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.UpdateMonitoredDirectoryRequest;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.directory_controller.CreateMonitoredDirectoryResponse;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.monitored_directory.MonitoredDirectoryRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note.NoteService;
import org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * Implementation of the {@link MonitoredDirectoryService} interface.
 */
@Service
public class MonitoredDirectoryServiceImpl
        extends PWSSbaseService<MonitoredDirectoryRepository, MonitoredDirectory, Integer>
        implements MonitoredDirectoryService {

    private final org.slf4j.Logger log;

    private final TimeService timeService;

    private final NoteService noteService;

    @Autowired
    public MonitoredDirectoryServiceImpl(MonitoredDirectoryRepository repository,
            TimeService timeService,
            NoteService noteService) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(MonitoredDirectoryServiceImpl.class);
        this.timeService = timeService;
        this.noteService = noteService;
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

        Optional<MonitoredDirectory> optional = repository.findById(id);
        return optional;
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

    @Transactional
    @Override
    public CreateMonitoredDirectoryResponse createMonitoredDirectoryFromRequest(
            CreateMonitoredDirectoryRequest createRequest) throws SecurityException, NullPointerException {

        if (validateRequest(createRequest)) {

            final Time time = new Time(OffsetDateTime.now(), OffsetDateTime.now());
            timeService.save(time);

            final Note note = new Note("Initial notes", time);
            noteService.save(note);

            try {
                Optional<MonitoredDirectory> oMonitoredDirectory = Optional
                        .of(repository.save(new MonitoredDirectory(createRequest.path(), createRequest.isActive(),
                                createRequest.includeSubdirectories(), time, note)));

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
    public Boolean updateMonitoredDirectoryFromRequest(UpdateMonitoredDirectoryRequest request)
            throws SecurityException {

        if (validateRequest(request)) {

            Optional<MonitoredDirectory> mOptional = repository.findById(request.id());

            if (mOptional.isPresent()) {

                MonitoredDirectory mDirectory = mOptional.get();

                final Time time = new Time(OffsetDateTime.now(), OffsetDateTime.now());

                timeService.save(time);

                final Note note = new Note(request.notes(), time);

                noteService.save(note);

                mDirectory.setNotes(note);

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