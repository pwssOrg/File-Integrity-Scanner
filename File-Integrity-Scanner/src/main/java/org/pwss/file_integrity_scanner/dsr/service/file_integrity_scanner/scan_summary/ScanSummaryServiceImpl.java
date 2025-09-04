package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForScanRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.SearchForFileRequest;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.scan_summary.ScanSummaryRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file.FileServiceImpl;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan.ScanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ScanSummaryServiceImpl extends PWSSbaseService<ScanSummaryRepository, ScanSummary, Long>
        implements ScanSummaryService {

    @Autowired
    private @Lazy ScanServiceImpl scanService;

    @Autowired
    private final FileServiceImpl fileService;

    private final org.slf4j.Logger log;

    @Autowired
    public ScanSummaryServiceImpl(ScanSummaryRepository repository, FileServiceImpl fileService) {
        super(repository);
        this.fileService = fileService;

        this.log = org.slf4j.LoggerFactory.getLogger(ScanSummaryServiceImpl.class);
    }

    @Override
    public void save(ScanSummary scanSummary) {
        repository.save(scanSummary);
    }

    @Override
    public List<ScanSummary> getScanSummaryForFile(GetSummaryForFileRequest request) throws SecurityException {

        if (validateRequest(request)) {

            final Optional<File> oPwssFile = fileService.findById(request.fileId());

            if (oPwssFile.isPresent()) {

                Optional<List<ScanSummary>> oScanSummaries = Optional.of(this.repository.findByFile(oPwssFile.get()));

                if (oScanSummaries.isPresent())
                    return oScanSummaries.get();
                else
                    return new LinkedList<>();

            } else {
                return new LinkedList<>();
            }

        }

        else {
            throw new SecurityException("Validation failed");
        }
    }

    @Override
    public List<ScanSummary> getScanSummaryForScan(GetSummaryForScanRequest request) throws SecurityException {

        if (validateRequest(request)) {

            Optional<Scan> oScan = scanService.findById(request.scanId());

            if (oScan.isPresent()) {

                Optional<List<ScanSummary>> oScanSummaries = Optional.of(this.repository.findByScan(oScan.get()));

                if (oScanSummaries.isPresent())
                    return oScanSummaries.get();
                else
                    return new LinkedList<>();
            }

            else {
                return new LinkedList<>();
            }
        } else
            throw new SecurityException();
    }

    @Override
    public List<ScanSummary> getMostRecentScanSummary() {

        Optional<Scan> oMostRecentScan = scanService.getMostRecentScan();

        if (oMostRecentScan.isPresent()) {
            Optional<List<ScanSummary>> oScanSummaries = Optional
                    .of(this.repository.findByScan(oMostRecentScan.get()));

            if (oScanSummaries.isPresent()) {
                return oScanSummaries.get();
            } else {
                return new LinkedList<>();
            }
        }
        return new LinkedList<>();
    }

    @Override
    public List<File> findFilesByBasenameLikeIgnoreCase(SearchForFileRequest request) throws SecurityException {

        if (validateRequest(request)) {

            List<File> listOfFiles = fileService.findFilesByBasenameLikeIgnoreCase(
                    request.searchQuery(),
                    request.limit(),
                    request.sortField(),
                    request.ascending());

            return listOfFiles;
        }

        else {
            throw new SecurityException("Validation failed");
        }

    }

    @Override
    public Optional<ScanSummary> findScanSummaryWithHighestIdWhereScanBaselineIsSetToTrue(File file) {

        final List<ScanSummary> baseLineScanSummaries = repository.findByFileAndScan_isBaselineScanTrue(file);

        if (!baseLineScanSummaries.isEmpty()) {
            log.debug("There exists - {} baseline scan summaries", baseLineScanSummaries.size());

            return Optional.of(baseLineScanSummaries.stream().max(Comparator.comparing(ScanSummary::getId)).get());
        }

        else
            return Optional.empty();
    }

}