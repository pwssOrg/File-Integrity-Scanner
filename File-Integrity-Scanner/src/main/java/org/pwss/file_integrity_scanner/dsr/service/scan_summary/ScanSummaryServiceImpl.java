package org.pwss.file_integrity_scanner.dsr.service.scan_summary;

import org.pwss.file_integrity_scanner.dsr.domain.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.repository.ScanSummaryRepository;
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