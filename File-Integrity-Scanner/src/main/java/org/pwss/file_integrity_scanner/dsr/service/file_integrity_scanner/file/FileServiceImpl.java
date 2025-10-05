package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller.QuarantineFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller.UnQurantineFileRequest;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.file.FileRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.QuarantineFailedException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.UnquarantineFailedException;
import org.pwss.quarantineManager_aes.dto.MetaDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl extends PWSSbaseService<FileRepository, File, Long> implements FileService {

    private final org.slf4j.Logger log;

    /**
     * Component responsible for handling file isolation operations,
     * such as quarantining and unquarantining files.
     */
    private final FileIsolationComponent fileIsolationComponent;

    /**
     * Constructor for the FileServiceImpl class. Initializes the service with
     * a repository and a file isolation component.
     *
     * @param repository             The repository used to access File entities in
     *                               the database.
     * @param fileIsolationComponent The component responsible for file isolation
     *                               operations.
     */
    public FileServiceImpl(FileRepository repository,
            @Autowired FileIsolationComponent fileIsolationComponent) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(FileServiceImpl.class);
        this.fileIsolationComponent = fileIsolationComponent;
    }

    @Override
    public File findByPath(String path) {
        Optional<File> mOptional = repository.findByPath(path);

        if (mOptional.isPresent()) {
            return mOptional.get();
        } else {
            log.info("File with path '{}' not found.", path);
            return null;
        }
    }

    @Override
    public boolean existsByPath(String path) {
        return repository.existsByPath(path);
    }

    @Override
    public void save(File file) {
        repository.save(file);
    }

    @Override
    public Optional<File> findById(Long id) {

        if (id != null)
            return repository.findById(id);
        else
            return Optional.empty();
    }

    @Override
    public List<File> findFilesByBasenameLikeIgnoreCase(String searchString, int limit, String sortField,
            boolean ascending) {

        final Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        final Sort sort = Sort.by(direction, sortField);

        final Pageable pageable = PageRequest.of(0, limit, sort);
        return repository.findByBasenameLikeIgnoreCase(searchString, pageable);
    }

    @Override
    public MetaDataResult quranantine(QuarantineFileRequest request)
            throws SecurityException, QuarantineFailedException {

        if (validateRequest(request)) {

            final File file;

            Optional<File> oFile = findById(request.fileId());

            if (oFile.isPresent()) {

                file = oFile.get();
                log.debug("File to be quarantined is found in the repository layer.\nIt is located at path: {}",
                        file.getPath());
            } else
                throw new QuarantineFailedException(
                        "File is not present in the repository layer and can not be used in quarantine or unquarantine operations");

            try {
                return fileIsolationComponent.quarantineFile(file);
            } catch (QuarantineFailedException quarantineFailedException) {
                throw new QuarantineFailedException(quarantineFailedException);
            }

        } else {
            throw new SecurityException("Validation failed!");
        }
    }

    @Override
    public MetaDataResult unQuarantine(UnQurantineFileRequest request)
            throws SecurityException, UnquarantineFailedException {
        if (validateRequest(request)) {

            try {
                return fileIsolationComponent.unquarantineFile(request.keyPath());
            } catch (UnquarantineFailedException unquarantineFailedException) {
                throw new UnquarantineFailedException(unquarantineFailedException);
            }

        } else {
            throw new SecurityException("Validation failed!");
        }
    }

}
