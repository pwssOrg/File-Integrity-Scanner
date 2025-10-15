package org.pwss.file_integrity_scanner.dsr.service.user_login.user;

import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller.LoginRequest;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.license.LicenseServiceImpl;
import org.pwss.file_integrity_scanner.exception.license.LicenseValidationFailedException;
import org.pwss.file_integrity_scanner.exception.user_login.CreateUserFailedException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.encoder.ByteArrayUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} that provides functionality related
 * to
 * user management.
 * This service interacts with an authentication service and a data repository
 * to perform operations
 * such as checking if users exist or if the system is empty.
 *
 * <p>
 * This class extends any necessary interfaces and provides concrete
 * implementations for abstract methods.
 * </p>
 */
@Service
public class UserServiceImpl
        extends
        PWSSbaseService<org.pwss.file_integrity_scanner.dsr.repository.user_login.user.UserRepository, User, Integer>
        implements UserService {

    /**
     * Service for authentication-related operations.
     */
    private final org.pwss.file_integrity_scanner.dsr.service.user_login.auth.AuthServiceImpl authService;

    /**
     * Service for time-related operations.
     */
    private final org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeServiceImpl timeService;

    /**
     * Service for license-related operations.
     */
    private final LicenseServiceImpl licenseService;

    private final org.slf4j.Logger log;

    private final String AUTHORITY = "AUTHORIZED";

    /**
     * Constructor for the UserServiceImpl class.
     *
     * @param repository     The repository for managing User entities.
     * @param authService    The service responsible for authentication operations.
     * @param timeService    The service responsible for time-related operations.
     * @param licenseService The service responsible for license management
     *                       operations.
     */
    @Autowired
    public UserServiceImpl(org.pwss.file_integrity_scanner.dsr.repository.user_login.user.UserRepository repository,
            org.pwss.file_integrity_scanner.dsr.service.user_login.auth.AuthServiceImpl authService,
            org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeServiceImpl timeService,
            LicenseServiceImpl licenseService) {

        super(repository);

        this.authService = authService;
        this.timeService = timeService;
        this.licenseService = licenseService;
        this.log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    }

    @Override
    public boolean ValidateCreateUserRequest(
            org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller.CreateUserRequest request) {

        boolean result;

        result = isEmpty();
        // More validate options for the CreateUser Request can be added here
        return result;
    }

    @Override
    public boolean CheckIfUsernameExists(String username) {
        return getUserByUsernamePrivate(username).stream().findAny().isPresent();
    }

    @Override
    public Optional<org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User> GetUserByUsername(
            String username) {
        return getUserByUsernamePrivate(username);
    }

    @Override
    @Transactional
    public org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User CreateUser(
            org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller.CreateUserRequest request)
            throws SecurityException, CreateUserFailedException, LicenseValidationFailedException {

        if (ValidateCreateUserRequest(request) && validateRequest(request.getUsername())
                && validateRequest(request.licenseKey())) {

            if (!licenseService.validateLicenseKey(request.licenseKey())) {
                throw new LicenseValidationFailedException("The license is invalid");
            }

            if (!validatePasswordForInjectionPoints(request.getPassword())) {

                throw new CreateUserFailedException("Your Password can not contain /* , */ or \\");
            }

            try {

                User user = new User();
                Auth auth = new Auth();
                Time time = new Time();

                String hashedPasswordWithSalt = GenerateHashWithSalt(request.getPassword());

                time.setCreated(OffsetDateTime.now());
                time.setUpdated(OffsetDateTime.now());

                Time authFromRepository = timeService.save(time);

                auth.setHash(hashedPasswordWithSalt);

                if (authFromRepository != null)
                    auth.setTime(authFromRepository);
                else
                   throw new CreateUserFailedException("Auth from repository is null");

                authService.save(auth);

                user.setUsername(request.getUsername());
                user.setTime(time);
                user.setAuth(auth);

                repository.save(user);

                return user;
            } catch (Exception ex) {
                log.error("Error occurred while creating a user {}", ex.getMessage());
                log.debug("Error occurred while creating a user {}", ex);
                throw new CreateUserFailedException("An exception occurred while creating a user");
            }

        } else {
            throw new SecurityException("Validation failed");
        }
    }

    @Override
    public boolean ValidatePassword(LoginRequest request)
            throws org.pwss.file_integrity_scanner.exception.user_login.UsernameNotFoundException, SecurityException,
            LicenseValidationFailedException {

        if (validateRequest(request.getUsername()) && validateRequest(request.licenseKey())
                && validatePasswordForInjectionPoints(request.getPassword())) {

            if (!licenseService.validateLicenseKey(request.licenseKey())) {
                throw new LicenseValidationFailedException("The license is invalid");
            }

            final String inputWord = request.getPassword();
            final String username = request.getUsername();

            final String storedPasswordHash = repository.findByUsername(username)
                    .orElseThrow(org.pwss.file_integrity_scanner.exception.user_login.UsernameNotFoundException::new)
                    .getAuth().getHash();

            if (CheckIfUsernameExists(username) && storedPasswordHash != null && storedPasswordHash.length() > 60 &&
                    storedPasswordHash.contains(":")) {

                String[] parts = storedPasswordHash.split(":");

                int iterations = Integer.valueOf(parts[0]);
                byte[] storedSalt = ByteArrayUtil.hexStringToByteArray(parts[1]);
                byte[] storedHash = ByteArrayUtil.hexStringToByteArray(parts[2]);

                byte[] inputWordPasswordHash;
                try {
                    inputWordPasswordHash = CreateHash(
                            inputWord,
                            storedSalt,
                            iterations);

                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }

                int diff = storedHash.length ^ inputWordPasswordHash.length;
                for (int i = 0; i < storedHash.length
                        && i < inputWordPasswordHash.length; i++) {

                    diff |= storedHash[i] ^ inputWordPasswordHash[i];
                }

                log.debug("Results of comparison between the login hash and the stored hash ->  {}", (diff == 0));

                return diff == 0;

            } else {
                return false;
            }

        } else {
            throw new SecurityException("Validation failed!");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws org.springframework.security.core.userdetails.UsernameNotFoundException {

        Optional<User> user = getUserByUsernamePrivate(username);

        if (user.isPresent())
            return BuildSpringSecurityUser(user.get());

        else
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("Username not found");

    }

    /**
     * Checks if both the authentication service and the data repository are empty.
     *
     * This method first queries the {@code authService} to determine if it is
     * empty.
     * If the {@code authService} reports that it is not empty, this method
     * immediately returns false.
     * Otherwise, it proceeds to query the {@code repository} to check if its count
     * is zero.
     * The final result of this method is true only if both the {@code authService}
     * and
     * the {@code repository} are empty, otherwise it returns false.
     *
     * @return {@code true} if both the authentication service and data repository
     *         are empty,
     *         {@code false} otherwise.
     */
    @Override
    public Boolean isEmpty() {

        boolean result = authService.isEmpty();

        return result && repository.count() == (0L);
    }

    private final Optional<User> getUserByUsernamePrivate(String username) {

        return repository.findByUsername(username);
    }

    /**
     * Converts an entity user into a Spring security user
     *
     * @param user pwss entity User
     * @return UserDetails
     */
    private org.springframework.security.core.userdetails.UserDetails BuildSpringSecurityUser(User user) {

        final boolean accountExpired = false;
        final boolean credentialsExpired = false;
        final boolean accountLocked = false;

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        authorities.add(new org.pwss.file_integrity_scanner.security.GrantedAuthorityImpl(AUTHORITY));

        final org.springframework.security.core.userdetails.UserDetails springSecurityUser = org.springframework.security.core.userdetails.User
                .builder()
                .accountExpired(accountExpired)
                .accountLocked(accountLocked)
                .credentialsExpired(credentialsExpired)
                .username(user.getUsername())
                .password(user.getAuth().getHash()) // {noop} indicates no password encoding
                .authorities(authorities)
                .build();
        return springSecurityUser;
    }

}