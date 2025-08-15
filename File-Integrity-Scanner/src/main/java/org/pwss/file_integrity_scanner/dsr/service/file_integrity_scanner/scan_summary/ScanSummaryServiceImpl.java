package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.ScanSummaryRepository;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ScanSummaryServiceImpl extends BaseService<ScanSummaryRepository> implements ScanSummaryService {

    public ScanSummaryServiceImpl(ScanSummaryRepository repository) {
        super(repository);
    }

    @Override
    public void save(ScanSummary scanSummary) {
        repository.save(scanSummary);
    }
}