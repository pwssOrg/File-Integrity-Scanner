package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.checksum;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.ChecksumRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChecksumServiceImpl extends PWSSbaseService<ChecksumRepository,Checksum, Long> implements ChecksumService {

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
            return Collections.emptyList();
        }

    }

    @Override
    public void save(Checksum checksum) {
        repository.save(checksum);
    }
}
