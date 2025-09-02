package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;

//TODO: Add Java Docs
public interface NoteService {

    /**
     * Saves a Note entity to the database.
     *
     * @param scanSummary the scan summary entity to save
     */
    void save(Note note);

    //TODO: Add Java Docs
    Boolean updateNote(Note note, String noteText);

    //TODO: Add Java Docs
    Boolean anyNoteContains(Note note, String text);

}
