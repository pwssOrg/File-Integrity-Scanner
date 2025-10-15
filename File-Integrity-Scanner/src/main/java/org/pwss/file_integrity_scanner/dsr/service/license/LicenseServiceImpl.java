package org.pwss.file_integrity_scanner.dsr.service.license;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.license.entities.License;
import org.pwss.file_integrity_scanner.dsr.repository.license.LicenseRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing license operations.
 *
 * This class provides functionality to validate license keys by interacting
 * with the repository layer and a dedicated license manager component.
 */
@Service
public class LicenseServiceImpl extends PWSSbaseService<LicenseRepository, License, Integer> implements LicenseService {

    /**
     * Logger for this service. Used to log information, warnings, errors, etc.
     */
    private final org.slf4j.Logger log;

    @Autowired
    private final LicenseComponent licenseManager;

    /**
     * Constructor for the LicenseServiceImpl.
     *
     * @param repository     The repository for managing License entities.
     * @param licenseManager The component responsible for license management
     *                       operations.
     */
    public LicenseServiceImpl(LicenseRepository repository,
            LicenseComponent licenseManager) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(LicenseServiceImpl.class);
        this.licenseManager = licenseManager;
    }

    @Override
    public final boolean validateLicenseKey(String licenseKey) {

        List<License> licenseList = this.repository.findAll();

        // Check for the correct number of licenses in the repository
        if (licenseList.size() != 2) {
            log.debug("The number of license data elements in the repository layer differs from the expected count.");
            return false;
        }

        // Validate against known license data values
        if (licenseList.get(0).getLicenseData()
                .equals("331cd7b3ce491feda6e855dcbf5de4dec5d5211e7776c8e7da4a91026ddab7b7")
                && licenseList.get(1).getLicenseData()
                        .equals("71b16c484b415c32e6139f95d7276ea351228fbef0af5f7dcbd8bff0484b59b5")) {

            String[] repositoryData = licenseList.stream().map(License::getLicenseData).toArray(String[]::new);

            try {
                return licenseManager.validateLicense(licenseKey, repositoryData);
            } catch (NoSuchAlgorithmException e) {
                log.error("Database error");
            }

        }

        else {
            log.debug("License key data in the repository layer does not match expected values.");
            return false;
        }

        log.debug("License key validation successful");
        return true;
    }

}
