package org.pwss.file_integrity_scanner;

import lib.pwss.cryptographic_algorithm.hash.FileHashHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class PWSSLibraryTest {

    @Test
    public void SHA256Test() throws URISyntaxException {

        final String expected="SHA-256: b952374f7966b97e7ac18228ff7b409a81bf2e7f1094fb557183365a721196dd";
        ClassLoader classLoader = getClass().getClassLoader();
        String resourcePath = "hi.txt";

        // Load the resource using the ClassLoader
        java.net.URL resourceUrl = classLoader.getResource(resourcePath);

        File file = Paths.get(resourceUrl.toURI()).toFile();
        FileHashHandler fileHashHandler = new FileHashHandler();
        final String actual = fileHashHandler.calculateSha256Hash(file);
        Assertions.assertEquals(expected,actual);
    }
    
}
