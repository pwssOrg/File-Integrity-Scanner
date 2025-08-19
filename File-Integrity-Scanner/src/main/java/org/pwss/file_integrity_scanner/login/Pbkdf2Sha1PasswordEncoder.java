package org.pwss.file_integrity_scanner.login;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A password encoder that uses the PBKDF2 (Password-Based Key Derivation
 * Function 2) algorithm with SHA-1.
 * This implementation follows the Spring Security PasswordEncoder interface to
 * provide encoding and matching
 * functionality for passwords.
 */
public class Pbkdf2Sha1PasswordEncoder implements PasswordEncoder {
    /**
     * Regular expression pattern to validate the format of encoded password
     * strings.
     * The expected format is: iterations:salt:hash
     */
    private static final Pattern FORMAT = Pattern.compile("^(\\d+):([0-9a-fA-F]+):([0-9a-fA-F]+)$");
    /**
     * Secure random number generator for generating cryptographic strength salts.
     */
    private static final SecureRandom RNG = new SecureRandom();
    /**
     * Hexadecimal format utility for encoding and decoding byte arrays to hex
     * strings.
     */
    private static final HexFormat HEX = HexFormat.of();

    @Override
    public String encode(CharSequence rawPassword) {
        int iterations = 1000;
        byte[] salt = new byte[16];
        RNG.nextBytes(salt);
        byte[] dk = pbkdf2(rawPassword, salt, iterations, 64); // 64 bytes == 512 bits
        return iterations + ":" + HEX.formatHex(salt) + ":" + HEX.formatHex(dk);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        Matcher m = FORMAT.matcher(encodedPassword);
        if (!m.matches())
            return false;

        int iterations = Integer.parseInt(m.group(1));
        byte[] salt = HEX.parseHex(m.group(2));
        byte[] expected = HEX.parseHex(m.group(3));

        byte[] actual = pbkdf2(rawPassword, salt, iterations, expected.length);
        return constantTimeEquals(expected, actual);
    }

    /**
     * Computes the PBKDF2 hash of a given password with the specified parameters.
     *
     * @param raw        The password to hash (as CharSequence)
     * @param salt       A byte array used as the salt value for the computation
     * @param iterations Number of iterations to apply the key derivation function
     * @param dkLenBytes Desired length of the derived key in bytes
     * @return Derived key as a byte array
     */
    private static byte[] pbkdf2(CharSequence raw, byte[] salt, int iterations, int dkLenBytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(raw.toString().toCharArray(), salt, iterations, dkLenBytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("PBKDF2(SHA1) failure", e);
        }
    }

    /**
     * Compares two byte arrays in constant time to prevent timing attacks.
     *
     * @param a First byte array
     * @param b Second byte array
     * @return true if both byte arrays are equal, false otherwise
     */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length)
            return false;
        int r = 0;
        for (int i = 0; i < a.length; i++)
            r |= a[i] ^ b[i];
        return r == 0;
    }
}
