package org.pwss.file_integrity_scanner.msr.service.checksum;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File;
import org.pwss.file_integrity_scanner.msr.repository.ChecksumRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ChecksumServiceImpl extends BaseService<ChecksumRepository> implements ChecksumService {

    private final org.slf4j.Logger log;

    public ChecksumServiceImpl(ChecksumRepository repository) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(ChecksumServiceImpl.class);
    }

    @Override
    public List<Checksum> findByFile(File file) {
        Optional<List<Checksum>> mOptional = repository.findByFile(file);

        if (mOptional.isPresent()) {
            return mOptional.get();
        } else {
            log.warn("No checksums found for file: {}", file.getPath());
            return null;
        }

    }

    @Override
    public void save(Checksum checksum) {
        repository.save(checksum);
    }
}
