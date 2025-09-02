package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

//TODO: Add Java Docs :P e :)
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
