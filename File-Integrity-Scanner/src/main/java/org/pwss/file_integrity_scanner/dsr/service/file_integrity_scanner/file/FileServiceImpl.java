package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.SearchForFileRequest;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.FileRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl extends PWSSbaseService<FileRepository, File, Long> implements FileService {

    private final org.slf4j.Logger log;

    public FileServiceImpl(FileRepository repository) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(FileServiceImpl.class);
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

}
