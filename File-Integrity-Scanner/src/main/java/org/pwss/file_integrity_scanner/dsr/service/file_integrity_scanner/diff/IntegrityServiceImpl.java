package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;

import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.diff.IntegrityRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;

import org.springframework.stereotype.Service;

//TODO: Add Java Docs
@Service
public class IntegrityServiceImpl extends PWSSbaseService<IntegrityRepository, Diff, Long> implements IntegrityService {

    private final org.slf4j.Logger log;

    public IntegrityServiceImpl(IntegrityRepository repository) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(IntegrityServiceImpl.class);
    }

    @Override
    public void save(Diff entity) {
        repository.save(entity);
    }

}
