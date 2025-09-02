package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.IntegrityDiffByScanRequest;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.diff.IntegrityRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

//TODO: Add Java Docs
@Service
public class IntegrityServiceImpl extends PWSSbaseService<IntegrityRepository, Diff, Long> implements IntegrityService {

    private final org.slf4j.Logger log;

    @Autowired
    private @Lazy ScanService scanService;

    
    public IntegrityServiceImpl(IntegrityRepository repository) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(IntegrityServiceImpl.class);
        
    }

    @Override
    public void save(Diff entity) {
        repository.save(entity);
    }

    @Override
    public List<Diff> retreiveDiffListFromScan(IntegrityDiffByScanRequest request) throws SecurityException {

        if (validateRequest(request)) {

            Optional<Scan> oScan = scanService.findById(request.scanId());

            if (oScan.isPresent()) {

                final Scan scan = oScan.get();

                final Sort.Direction direction = request.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                final Sort sort = Sort.by(direction, request.sortField());

                final Pageable pageable = PageRequest.of(0, request.limit(), sort);

                return this.repository.findByIntegrityFail_Scan(scan, pageable);

            }

            else
                return new LinkedList<>();
        } else
            throw new SecurityException("Validation failed!");

    }

}
