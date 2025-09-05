package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Note} entities.
 * This interface provides methods to perform CRUD operations on Note entities,
 * as well as custom query methods if needed in the future.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
