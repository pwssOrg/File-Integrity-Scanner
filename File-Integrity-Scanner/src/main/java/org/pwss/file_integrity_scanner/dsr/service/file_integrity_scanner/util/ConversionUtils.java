package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.util;

/**
 * Utility class providing methods to convert between different units of data
 * measurement, such as bytes,
 * megabytes, and gigabytes. This class includes static utility methods that
 * perform these conversions.
 *
 * <p>
 * The primary purpose of this class is to facilitate unit conversions in
 * scenarios where file sizes or
 * other data measurements need to be handled in a specific unit for easier
 * comprehension or processing.
 * </p>
 */
public class ConversionUtils {
    private static final long MEGABYTE = 1024L * 1024L;
    private static final long GIGABYTE = MEGABYTE * 1024L;

    /**
     * Converts a value in bytes to megabytes.
     *
     * @param bytes the number of bytes to convert
     * @return the equivalent value in megabytes as a Long
     */
    public static Long bytesToMegabytes(Long bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Bytes value cannot be null.");
        }
        return bytes / MEGABYTE;
    }

    /**
     * Converts a value in megabytes to bytes.
     *
     * @param megabytes the number of megabytes to convert
     * @return the equivalent value in bytes as a Long
     */
    public static Long megabytesToBytes(Long megabytes) {
        if (megabytes == null) {
            throw new IllegalArgumentException("Megabytes value cannot be null.");
        }
        return megabytes * MEGABYTE;
    }

    // Optional: If you want to handle conversion with double values for more
    // precise calculations
    /**
     * Converts a value in bytes to megabytes.
     *
     * @param bytes the number of bytes to convert
     * @return the equivalent value in megabytes as a Double
     */
    public static Double bytesToMegabytesDouble(Long bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Bytes value cannot be null.");
        }
        return (double) bytes / MEGABYTE;
    }

    /**
     * Converts a value in megabytes to bytes.
     *
     * @param megabytes the number of megabytes to convert
     * @return the equivalent value in bytes as a Double
     */
    public static Long megabytesToBytesDouble(Double megabytes) {
        if (megabytes == null) {
            throw new IllegalArgumentException("Megabytes value cannot be null.");
        }
        return Math.round(megabytes * MEGABYTE);
    }

    /**
     * Converts a value in bytes to gigabytes.
     *
     * @param bytes the number of bytes to convert
     * @return the equivalent value in gigabytes as a Double
     */
    public static Double bytesToGigabytesDouble(Long bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Bytes value cannot be null.");
        }
        return (double) bytes / GIGABYTE;
    }

    /**
     * Converts a value in gigabytes to bytes.
     *
     * @param gigabytes the number of gigabytes to convert
     * @return the equivalent value in bytes as a Double
     */
    public static Long gigabytesToBytesDouble(Double gigabytes) {
        if (gigabytes == null) {
            throw new IllegalArgumentException("Gigabytes value cannot be null.");
        }
        return Math.round(gigabytes * GIGABYTE);
    }

}
