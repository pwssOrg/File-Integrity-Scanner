package org.pwss.file_integrity_scanner.login;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Pbkdf2Sha1PasswordEncoder implements PasswordEncoder {
    private static final Pattern FORMAT = Pattern.compile("^(\\d+):([0-9a-fA-F]+):([0-9a-fA-F]+)$");
    private static final SecureRandom RNG = new SecureRandom();
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
        var m = FORMAT.matcher(encodedPassword);
        if (!m.matches())
            return false;

        int iterations = Integer.parseInt(m.group(1));
        byte[] salt = HEX.parseHex(m.group(2));
        byte[] expected = HEX.parseHex(m.group(3));

        byte[] actual = pbkdf2(rawPassword, salt, iterations, expected.length);
        return constantTimeEquals(expected, actual);
    }

    private static byte[] pbkdf2(CharSequence raw, byte[] salt, int iterations, int dkLenBytes) {
        try {
            var spec = new PBEKeySpec(raw.toString().toCharArray(), salt, iterations, dkLenBytes * 8);
            var skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("PBKDF2(SHA1) failure", e);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length)
            return false;
        int r = 0;
        for (int i = 0; i < a.length; i++)
            r |= a[i] ^ b[i];
        return r == 0;
    }
}
