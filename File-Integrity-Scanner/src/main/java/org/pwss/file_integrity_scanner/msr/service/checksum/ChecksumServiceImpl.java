package org.pwss.file_integrity_scanner.msr.service.checksum;

import org.pwss.file_integrity_scanner.msr.repository.ChecksumRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;


@Service
public class ChecksumServiceImpl extends BaseService<ChecksumRepository> implements ChecksumService {

    public ChecksumServiceImpl(ChecksumRepository repository) {
        super(repository);
    }
}
