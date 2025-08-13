package org.pwss.file_integrity_scanner.dsr.service.user_login.user;



import org.apache.tomcat.util.buf.HexUtils;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.pwss.file_integrity_scanner.login.exception.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

public interface UserService extends UserDetailsService {


    /**
     * Creates a Hash String from an input password and splits it into 3 parts (Hash,Salt,Iterations)
     *
     * @param password A clear text password
     * @return String
     * @throws NoSuchAlgorithmException If no such algorithm exists
     * @throws InvalidKeySpecException  If a key in the clear text -> hash mechanism would be invalid
     */
    default String GenerateHashWithSalt(final String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        final int iterations = 1000;

        byte[] salt = GenerateSalt();

        byte[] hash = CreateHash(password, salt, iterations);

        return iterations + ":"
                + HexUtils.toHexString(salt) + ":"
                + HexUtils.toHexString(hash);
    }

    /**
     * Generates a random salt
     *
     * @return byte[]
     */
    private byte[] GenerateSalt() {

        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        byte[] salt = new byte[16];

        random.nextBytes(salt);
        return salt;
    }

    /**
     * Generates a hash with salt
     *
     * @param password A clear text password
     * @param salt     A salt that should make identical clear words into different hashes
     * @return byte[]
     * @throws NoSuchAlgorithmException If no such algorithm exists
     * @throws InvalidKeySpecException  If a key in the clear text -> hash mechanism would be invalid
     */
    default byte[] CreateHash(
            final String password,
            final byte[] salt,
            final int iterations)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException {


        final int keyLength = 64;

        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(),
                salt, iterations, keyLength * 8);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");


        return secretKeyFactory.generateSecret(keySpec).getEncoded();

    }

    /**
     * Creates a user if validation passes
     *
     * @param request A CreateUser request object
     * @return User
     * @throws NoSuchAlgorithmException If no such algorithm exists
     * @throws InvalidKeySpecException  If a key in the clear text -> hash mechanism would be invalid
     */
    User CreateUser(org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.CreateUser request) throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Validate the form data of a client request
     *
     * @param request A CreateUser request object
     * @return boolean
     */
    boolean ValidateCreateUserRequest(org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.CreateUser request);

    /**
     * Checks if a username already exists in the persistence layer
     *
     * @param username A String that contains a username
     * @return boolean
     */
    boolean CheckIfUsernameExists(String username);

    /**
     * Get a user by username
     *
     * @param username A String that contains a username
     * @return Optional<User>
     * @apiNote The entity is wrapped by an Optional class for null safety
     */
    Optional<User> GetUserByUsername(String username);

    /**
     * Validate the input word against the stored hash
     *
     * @param inputWord Clear text password
     * @param userName  Pwss Username
     * @return boolean
     */
    boolean ValidatePassword(final String inputWord, String userName) throws UsernameNotFoundException;
}