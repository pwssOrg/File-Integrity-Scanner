package org.pwss.file_integrity_scanner.msr.service.scan_details;

import org.pwss.file_integrity_scanner.msr.repository.ChecksumRepository;
import org.pwss.file_integrity_scanner.msr.repository.ScanDetailsRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ScanDetailsServiceImpl extends BaseService<ScanDetailsRepository> implements ScanDetailsService {

    public ScanDetailsServiceImpl(ScanDetailsRepository repository) {
        super(repository);
    }
}