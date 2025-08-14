package org.pwss.file_integrity_scanner.dsr.service.user_login.user;


import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;

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
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl
        extends BaseService<org.pwss.file_integrity_scanner.dsr.repository.user_login.user.UserRepository>
        implements UserService {

    private final org.pwss.file_integrity_scanner.dsr.service.user_login.auth.AuthServiceImpl authService;

    private final org.pwss.file_integrity_scanner.dsr.service.user_login.time.TimeServiceImpl timeService;

    private final org.slf4j.Logger log;

    private final String AUTHORITY = "AUTHORIZED";

    @Autowired
    public UserServiceImpl(org.pwss.file_integrity_scanner.dsr.repository.user_login.user.UserRepository repository,
            org.pwss.file_integrity_scanner.dsr.service.user_login.auth.AuthServiceImpl authService,
            org.pwss.file_integrity_scanner.dsr.service.user_login.time.TimeServiceImpl timeService) {

        super(repository);

        this.authService = authService;
        this.timeService = timeService;
        this.log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    }

    @Override
    public boolean ValidateCreateUserRequest(
            org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.CreateUser request) {
        // TODO: Create validator here
        return true;
    }

    private final Optional<User> getUserByUsernamePrivate(String username) {

        return repository.findByUsername(username);
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
            org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.CreateUser request)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        if (ValidateCreateUserRequest(request)) {

            try {

                User user = new User();
                Auth auth = new Auth();
                Time time = new Time();

                String hashedPasswordWithSalt = GenerateHashWithSalt(request.password);

                time.setCreated(OffsetDateTime.now());
                time.setUpdated(OffsetDateTime.now());

                Time authFromRepository = timeService.save(time);

                auth.hash = hashedPasswordWithSalt;

                if (authFromRepository != null)
                    auth.time = authFromRepository;
                else
                    return null;

                authService.save(auth);

                user.username = request.username;
                user.time = time;
                user.auth = auth;

                repository.save(user);

                return user;
            } catch (Exception ex) {
                log.error("Error occurred while creating a user {}", ex.getMessage());
            }

        }
        return null;
    }

    @Override
    @Transactional
    public boolean ValidatePassword(String inputWord, String username)
            throws org.pwss.file_integrity_scanner.login.exception.UsernameNotFoundException {

        final String storedPasswordHash = repository.findByUsername(username)
                .orElseThrow(org.pwss.file_integrity_scanner.login.exception.UsernameNotFoundException::new).auth.hash;

        if (CheckIfUsernameExists(username) && storedPasswordHash != null ||
                Objects.equals(storedPasswordHash, "")) {

            log.debug("Stored Password Hash",storedPasswordHash);

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

            log.debug("diff ->  {}" ,(diff == 0));

            return diff == 0;

        } else {
            return false;
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

        authorities.add(new org.pwss.file_integrity_scanner.login.GrantedAuthorityImpl(AUTHORITY));

        final  org.springframework.security.core.userdetails.UserDetails springSecurityUser =  org.springframework.security.core.userdetails.User.builder()
                 .accountExpired(accountExpired)
                 .accountLocked(accountLocked)
                 .credentialsExpired(credentialsExpired)
                 .username(user.getUsername())
                 .password(user.auth.getHash()) // {noop} indicates no password encoding
                 .authorities(authorities)
                 .build();
        return springSecurityUser;
    }



}