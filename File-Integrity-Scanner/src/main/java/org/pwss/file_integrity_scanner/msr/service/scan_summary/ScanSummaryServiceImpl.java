package org.pwss.file_integrity_scanner.msr.service.scan_summary;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.msr.repository.ScanSummaryRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
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