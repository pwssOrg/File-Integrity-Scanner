package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.ScanIntegrityDiffRequest;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.diff.IntegrityRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IntegrityService interface.
 * This service handles operations related to integrity diff entities,
 * including saving and retrieving them from the database, as well as
 * interacting with scan services.
 */
@Service
public class IntegrityServiceImpl extends PWSSbaseService<IntegrityRepository, Diff, Long> implements IntegrityService {

    /**
     * Logger for this service. Used to log information, warnings, errors, etc.
     */
    private final org.slf4j.Logger log;

    /**
     * Scan service used to perform operations related to scans.
     * This is autowired and lazily initialized to avoid unnecessary initialization
     * if not needed.
     */
    @Autowired
    private @Lazy ScanService scanService;

    /**
     * Constructor for IntegrityServiceImpl.
     *
     * @param repository The {@link IntegrityRepository} used to perform database
     *                   operations on Diff entities.
     */
    public IntegrityServiceImpl(IntegrityRepository repository) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(IntegrityServiceImpl.class);

    }

    @Override
    public void save(Diff entity) {
        repository.save(entity);
    }

    @Override
    public List<Diff> retreiveDiffListFromScan(ScanIntegrityDiffRequest request) throws SecurityException {

        if (validateRequest(request)) {

            Optional<Scan> oScan = scanService.findById(request.scanId());

            if (oScan.isPresent()) {

                final Scan scan = oScan.get();

                final Sort.Direction direction = request.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                final Sort sort = Sort.by(direction, request.sortField());

                final Pageable pageable = PageRequest.of(0, request.limit(), sort);

                return this.repository.findByIntegrityFail_Scan(scan, pageable);

            }

            else {
                log.debug("The Scan object was not present for ID {}", request.scanId());
                return new LinkedList<>();
            }
        } else
            throw new SecurityException("Validation failed!");

    }

}
