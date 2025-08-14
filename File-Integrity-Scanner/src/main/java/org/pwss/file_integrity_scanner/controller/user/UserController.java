package org.pwss.file_integrity_scanner.controller.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.CreateUser;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.LoginRequest;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.model.response.LoginResponse;
import org.pwss.file_integrity_scanner.dsr.service.user_login.user.UserServiceImpl;
import org.pwss.file_integrity_scanner.login.exception.UsernameNotFoundException;
import org.pwss.file_integrity_scanner.login.exception.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    private final org.slf4j.Logger log;

    @Autowired
    private final UserServiceImpl service;

    public UserController(UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager) {
        this.service = userServiceImpl;
        this.log = org.slf4j.LoggerFactory.getLogger(UserController.class);
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest request, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse)
            throws UsernameNotFoundException, WrongPasswordException {

        log.debug("in login Controller");
        Authentication authenticationBefore = SecurityContextHolder.getContext().getAuthentication();
        log.debug("isAuthenticated: {} ", authenticationBefore.isAuthenticated());
        authenticationBefore.getAuthorities().stream().toList().forEach(a -> System.out.println(a.getAuthority()));

        log.debug(authenticationBefore.getCredentials().toString());

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

                java.util.List<String> roles = authentication.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).toList();

                UserDetails ud = service.loadUserByUsername(request.getUsername());

                log.debug("from returned UserDetails {} ",
                        ud.getAuthorities().stream().findFirst().get().getAuthority());

                log.debug("Authorities: {} ", authentication.isAuthenticated());

                return new ResponseEntity<>(new LoginResponse(true), HttpStatus.ACCEPTED);

            } else
                throw new WrongPasswordException();
        } catch (WrongPasswordException passwordException) {

            return new ResponseEntity<LoginResponse>(new LoginResponse(false), HttpStatus.NOT_FOUND);
        } catch (UsernameNotFoundException notFoundException) {

            return new ResponseEntity<>(new LoginResponse(false), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> CreateUser(@RequestBody CreateUser request)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        User user = service.CreateUser(request);

        if (user == null) {
            throw new Error();
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

}