package org.pwss.file_integrity_scanner.dsr.service.file;

import org.pwss.file_integrity_scanner.dsr.domain.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.repository.FileRepository;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileServiceImpl extends BaseService<FileRepository> implements FileService {

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
}
