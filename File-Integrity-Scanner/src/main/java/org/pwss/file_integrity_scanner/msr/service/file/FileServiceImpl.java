package org.pwss.file_integrity_scanner.msr.service.file;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File;
import org.pwss.file_integrity_scanner.msr.repository.FileRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileServiceImpl extends BaseService<FileRepository> implements FileService {

    public FileServiceImpl(FileRepository repository) {
        super(repository);
    }

    @Override
    public File findByPath(String path) {
        Optional<File> mOptional = repository.findByPath(path);

        if (mOptional.isPresent()) {
            return mOptional.get();
        } else {
            // Write a ERROR log message (you can add the same log dependency as in PWSS File Nav)
            // I will create a ticket for it :)
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
