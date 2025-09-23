package org.pwss.file_integrity_scanner.dsr.service;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Base service class providing common functionality for all services that
 * interact with repositories.
 *
 * This abstract class defines a generic repository-based service pattern which
 * can be extended
 * by concrete implementations to provide specific business logic while reusing
 * the common CRUD operations.
 *
 * @param <Repository> The type of JPA repository used by this service. Must
 *                     extend {@link JpaRepository}.
 * @param <T>          The entity type managed by the repository. Must extend
 *                     {@link PWSSbaseEntity}.
 * @param <ID>         The type of identifier for entities in the repository.
 *                     Must extend {@link Number}.
 * @author PWSS ORG
 */
public abstract class PWSSbaseService<Repository extends JpaRepository<T, ID>, T extends PWSSbaseEntity, ID extends Number> {

    /** The repository used by this service to perform database operations. */
    protected final Repository repository;

    private final org.slf4j.Logger logForPWSSBaseClass;

    /**
     * Constructs a new instance of the service with the specified repository.
     *
     * @param repository The repository that will be managed by this service.
     */
    @Autowired(required = false) // Optional if repository might not be injected in some cases
    protected PWSSbaseService(Repository repository) {
        this.repository = repository;
        this.logForPWSSBaseClass = org.slf4j.LoggerFactory.getLogger(PWSSbaseService.class);
    }

    /**
     * Validates a given request object.
     *
     * This method checks if the provided object is null and handles it explicitly
     * by returning false.
     * It then determines the type of the object (collection, map, array, or general
     * object) and delegates
     * validation to the appropriate helper method. If any exception occurs during
     * validation,
     * it logs an error message and returns false.
     *
     * @param object The request object to be validated.
     * @return True if the object is valid according to its type-specific validation
     *         rules; False otherwise.
     */
    protected boolean validateRequest(Object object) {

        logForPWSSBaseClass.debug("Validating request object...");

        // Handle null case explicitly
        if (object == null) {
            return false;
        }

        try {
            // Check for arrays and collections
            if (object instanceof Collection) {
                return validateCollection((Collection<?>) object);
            } else if (object instanceof Map) {
                return validateMap((Map<?, ?>) object);
            } else if (object.getClass().isArray()) {
                return validateArray(object);

            }

            else if (object.getClass().isEnum()) {
                return validateEnum(object);
            }

            else {
                // Handle non-collection objects
                return validateObject(object);
            }
        } catch (Exception e) {
            // Log the exception and consider it invalid if validation fails
            logForPWSSBaseClass.error("Validation error - {}", e.getMessage());
            return false;
        }
    }

    /**
     * Validates a collection of objects.
     *
     * This method iterates through each item in the collection and recursively
     * calls
     * {@link #validateRequest(Object)} to validate it. If any item is invalid,
     * it returns false; otherwise, it returns true.
     *
     * @param collection The collection of objects to be validated.
     * @return True if all items in the collection are valid; False otherwise.
     */
    private boolean validateCollection(Collection<?> collection) {
        for (Object item : collection) {
            if (!validateRequest(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates whether an object is a valid enum instance.
     *
     * This method checks if the provided object is not null and its class is an
     * enumeration type. Enums in Java are special classes that represent fixed sets
     * of constants, and this validation ensures that the object belongs to such
     * a class.
     *
     * @param obj the object to validate as an enum instance
     * @return true if the object is a valid non-null enum instance, false otherwise
     */
    private boolean validateEnum(Object obj) {

        return obj != null && obj.getClass().isEnum();

    }

    /**
     * Validates a map of key-value pairs.
     *
     * This method iterates through each entry in the map and recursively calls
     * {@link #validateRequest(Object)} to validate both the key and value. If any
     * key or value is invalid, it returns false; otherwise, it returns true.
     *
     * @param map The map to be validated.
     * @return True if all keys and values in the map are valid; False otherwise.
     */
    private boolean validateMap(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!validateRequest(entry.getKey())) {
                return false;
            }
            if (!validateRequest(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates an array of objects.
     *
     * This method iterates through each element in the array and recursively calls
     * {@link #validateRequest(Object)} to validate it. If any element is invalid,
     * it returns false; otherwise, it returns true.
     *
     * @param array The array to be validated.
     * @return True if all elements in the array are valid; False otherwise.
     */
    private boolean validateArray(Object array) {
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            if (!validateRequest(item)) {
                return false;
            }
        }
        return true;

    }

    /**
     * Validates a general object.
     *
     * This method first checks if the object is of a primitive type or wrapper
     * class.
     * If it is, it returns true. Otherwise, it recursively validates each field in
     * the object by calling {@link #validateRequest(Object)} on each field's value.
     * If any field value is invalid, it logs an error and returns false; otherwise,
     * it returns true.
     *
     * @param object The general object to be validated.
     * @return True if all fields of the object are valid; False otherwise.
     */
    private boolean validateObject(Object object) {
        Class<?> clazz = object.getClass();

        // Check for primitive types and wrappers
        if (isPrimitiveType(clazz)) {

            if (clazz == String.class) {

                if (!validateStringLength((String) object))
                    return false;

                if (!validateForInjection((String) object))
                    return false;

                return true;
            }

            else
                return true;
        }

        // For other objects, recursively check their fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (!validateRequest(value)) {
                    return false;
                }
            } catch (Exception e) {
                logForPWSSBaseClass.error("Error accessing field - {} - {}", field.getName(), e.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a given class is a primitive type or wrapper class.
     *
     * @param clazz The class to be checked.
     * @return True if the class is a primitive type or wrapper class; False
     *         otherwise.
     */
    private boolean isPrimitiveType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class == clazz ||
                Character.class == clazz ||
                Byte.class == clazz ||
                Short.class == clazz ||
                Integer.class == clazz ||
                Long.class == clazz ||
                Float.class == clazz ||
                Double.class == clazz;
    }

    /**
     * Validates if a given string has an acceptable length.
     *
     * The string is considered valid if its length does not exceed the maximum
     * allowed length.
     *
     * @param input the string to be validated
     * @return true if the string's length is within the acceptable range, false
     *         otherwise
     */
    private boolean validateStringLength(String input) {

        final int maxLength = 10000;

        return input.length() <= maxLength;
    }

    /**
     * Validates a string input to check for potential injection attack patterns.
     * This method looks for common dangerous patterns that could indicate
     * an attempt at SQL or command injection. Note that this is not foolproof
     * and should be used in conjunction with other security measures such as
     * parameterized queries and proper input sanitization.
     *
     * @param input the string to validate for potential injection attacks
     * @return true if the input does not contain any dangerous patterns, false
     *         otherwise
     */
    private boolean validateForInjection(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }

        // Basic pattern to detect common injection patterns. This is not foolproof.
        final String[] dangerousPatterns = { ";", "--", "/*", "*/", "@@", "@", "\\", "$" };

        for (String pattern : dangerousPatterns) {
            if (input.contains(pattern)) {
                logForPWSSBaseClass.error("Potential Java Injection attack stopped!");
                return false;
            }
        }
        return true;
    }

}
