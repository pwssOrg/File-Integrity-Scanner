package org.pwss.file_integrity_scanner.controller.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller.CreateUserRequest;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller.LoginRequest;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.response.user_controller.LoginResponse;
import org.pwss.file_integrity_scanner.dsr.service.user_login.user.UserServiceImpl;
import org.pwss.file_integrity_scanner.exception.user_login.UsernameNotFoundException;
import org.pwss.file_integrity_scanner.exception.user_login.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * REST controller for handling user-related operations.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    private final org.slf4j.Logger log;

    @Autowired
    private final UserServiceImpl service;

    /**
     * Constructor for UserController.
     *
     * @param userServiceImpl       the service to handle user operations.
     * @param authenticationManager the manager for authentication.
     */
    public UserController(UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager) {
        this.service = userServiceImpl;
        this.log = org.slf4j.LoggerFactory.getLogger(UserController.class);
        this.authenticationManager = authenticationManager;
    }

    /**
     * Handles user login operations.
     *
     * @param request      the login request containing username and password.
     * @param httpRequest  the HTTP servlet request.
     * @param httpResponse the HTTP servlet response.
     * @return a ResponseEntity with the result of the login attempt.
     * @throws UsernameNotFoundException if the user is not found.
     * @throws WrongPasswordException    if the password is incorrect.
     */
    @Operation(summary = "User login", description = "Handles user login operations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest request, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse)
            throws UsernameNotFoundException, WrongPasswordException {

        try {

            Boolean loginValid = service.ValidatePassword(request.getPassword(), request.getUsername());

            log.debug("Login is {} ", loginValid);
            if (loginValid) {

                Authentication authRequest = new UsernamePasswordAuthenticationToken(request.username(),
                        request.password());
                Authentication authentication = authenticationManager.authenticate(authRequest);

                // Put the Authentication into the SecurityContext…
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

                // …and persist it to the HTTP session so the user stays logged in
                securityContextRepository.saveContext(context, httpRequest, httpResponse);

                UserDetails ud = service.loadUserByUsername(request.getUsername());

                log.debug("from returned UserDetails {} ",
                        ud.getAuthorities().stream().findFirst().get().getAuthority());

                return new ResponseEntity<>(new LoginResponse(true), HttpStatus.OK);

            } else
                throw new WrongPasswordException();
        } catch (WrongPasswordException passwordException) {
            return new ResponseEntity<LoginResponse>(new LoginResponse(false), HttpStatus.NOT_FOUND);
        } catch (UsernameNotFoundException notFoundException) {

            log.debug("User with username - {} was not found.", request.getUsername(), notFoundException);

            return new ResponseEntity<>(new LoginResponse(false), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Handles user creation operations.
     *
     * @param request the create user request containing user details.
     * @return a ResponseEntity with the result of the creation operation or a
     *         status code indicating the outcome.
     * @throws NoSuchAlgorithmException if there is an issue with the algorithm used
     *                                  for password hashing.
     * @throws InvalidKeySpecException  if the key specification is invalid.
     */
    @Operation(summary = "Create a new user", description = "Handles user creation operations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "A user is already present in the repository"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<User> CreateUser(@RequestBody CreateUserRequest request)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        if (!service.isEmpty()) {
            log.debug("A user is already present in the repository");
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }

        User user = service.CreateUser(request);

        if (user == null) {
            throw new IllegalStateException("Failed to create user: service returned null.");
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    /**
     * Checks if a user exists in the system.
     *
     * @return A {@link ResponseEntity} containing a boolean indicating whether a
     *         user exists or not, with appropriate HTTP status codes:
     *         - {@code 200 OK} if a user exists
     *         - {@code 404 NOT_FOUND} if no user is found
     */
    @Operation(summary = "Check if a user exists", description = "Checks if at least one user exists in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User exists"),
            @ApiResponse(responseCode = "404", description = "No user found"),
    })
    @GetMapping("/exists")
    public ResponseEntity<Boolean> userExistsCheck() {

        final Boolean isEmpty = service.isEmpty();
        if (isEmpty) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }
}
