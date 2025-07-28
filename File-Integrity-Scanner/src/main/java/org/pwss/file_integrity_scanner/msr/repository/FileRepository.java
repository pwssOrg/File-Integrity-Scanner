package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

   /**
    * Finds a file entity by its path.
    *
    * @param path the file path to search for
    * @return the file entity with the given path, or null if not found
    */
   Optional<File> findByPath(String path);

   /**
    * Checks if a file entity exists with the given path.
    *
    * @param path the file path to check
    * @return true if a file with the given path exists, false otherwise
    */
   boolean existsByPath(String path);
}
