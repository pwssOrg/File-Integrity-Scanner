package org.pwss.file_integrity_scanner.msr.service.file;

import org.pwss.file_integrity_scanner.msr.repository.FileRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends BaseService<FileRepository> implements FileService {

    public FileServiceImpl(FileRepository repository) {
        super(repository);
    }

}
