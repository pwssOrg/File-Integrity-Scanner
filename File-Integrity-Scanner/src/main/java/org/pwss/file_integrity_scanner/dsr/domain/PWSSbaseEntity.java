package org.pwss.file_integrity_scanner.dsr.domain;

import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;

/**
 * Base entity class for entities within the PWSS File-Integrity-Scanner
 * application.
 *
 * This abstract class serves as a marker class for the {@link PWSSbaseService}
 * class, helping to limit
 * the types that the generic parameter T can have. It also provides constants
 * to indicate which
 * functional section of the application an entity belongs to: either
 * File-Integrity-Scanner or User-Login.
 *
 * <p>
 * This is a good choice for adding common functionality to various entity
 * classes, ensuring
 * consistency and reducing code duplication across different entities.
 * </p>
 *
 * <p>
 * Implementing this class also allows entities to participate in the
 * application's service layer,
 * where the {@link PWSSbaseService} can leverage these constants and methods for
 * better organization
 * and functionality management.
 * </p>
 * @author PWSS ORG
 */
public abstract class PWSSbaseEntity {

    /**
     * Constant representing the File-Integrity-Scanner section of the application.
     * This constant is used to indicate that an entity belongs to the file
     * integrity scanner functionality.
     *
     * <p>
     * When implementing this base class, if your entity table belongs to the
     * File Integrity Scanner functionality, you should return this value in the
     * {@link #getDBSection()}
     * method.
     * </p>
     */
    protected final String FIS = "File-Integrity-Scanner";

    /**
     * Constant representing the User-Login section of the application.
     * This constant is used to indicate that an entity belongs to the user login
     * functionality.
     *
     * <p>
     * When implementing this base class, if your entity table belongs to the
     * User Login functionality, you should return this value in the
     * {@link #getDBSection()}
     * method.
     * </p>
     */
    protected final String USER_LOGIN = "User-Login";

    /**
     * Abstract method that must be implemented by any subclass of PWSSBaseEntity.
     * This method returns a string indicating which section of the application
     * the entity belongs to, either {@link #FIS} or {@link #USER_LOGIN}.
     *
     * <p>
     * This method is used to inform clients (e.g., methods in implementing classes
     * or service invokers)
     * about the functional section that the database table this entity represents
     * belongs to.
     * </p>
     *
     * @return The section of the application this entity belongs to. Either
     *         {@link #FIS} or {@link #USER_LOGIN}.
     */
    protected abstract String getDBSection();

}
