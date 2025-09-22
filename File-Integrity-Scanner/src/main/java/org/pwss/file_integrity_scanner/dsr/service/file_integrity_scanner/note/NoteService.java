package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller.RestoreNoteRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller.UpdateNoteRequest;

/**
 * Service interface for managing Note entities.
 * This interface provides methods for saving, updating,
 * and checking note entities in the database.
 */
public interface NoteService {
    /**
     * Saves a Note entity to the database.
     *
     * @param note the {@link Note} entity to save
     */
    void save(Note note);

    /**
     * Updates an existing Note with new text content.
     *
     * This method performs a series of checks and updates on the note entity. It
     * ensures that:
     * 1. The note exists in the repository layer (has an ID).
     * 2. The note's current contents are properly managed, updating them as
     * necessary while preserving previous
     * versions.
     *
     * If any errors occur during these operations (such as null values), they are
     * logged appropriately.
     *
     * @param note     the {@link Note} entity to update
     * @param noteText the new text content for the note
     * @return a Boolean indicating whether the update was successful
     */
    Boolean updateNote(Note note, String noteText);

    /**
     * Updates a note with new information provided in the request.
     *
     * @param request The UpdateNoteRequest object containing data for updating the
     *                note.
     * @return True if the note was successfully updated, false otherwise.
     * @throws SecurityException If validation of the request fails or any
     *                           security-related issue occurs.
     */
    Boolean updateNote(UpdateNoteRequest request) throws SecurityException;

    /**
     * Checks if any of the three notes contained within the Note entity contains
     * the specified text.
     *
     * This method looks for the provided text in:
     * 1. The main note (current content)
     * 2. The previous note
     * 3. The second previous note
     *
     * @param note the {@link Note} entity to check within
     * @param text the text to look for within any of the note fields
     * @return a Boolean indicating whether the text is found in any of the note
     *         fields
     */
    Boolean anyNoteContains(Note note, String text);

    /**
     * Restores a previous version of the given note based on the specified restore
     * type.
     *
     * This method first validates the request. If validation fails, a
     * {@link SecurityException} is thrown.
     * It then checks if the note exists in the repository layer by verifying its
     * ID.
     * If the note does not exist or the restore type is invalid, appropriate
     * exceptions are thrown.
     * Depending on the specified {@code RestoreNoteRequest}, it restores either
     * the previous version of the note (PREV_NOTE) or the prior previous version
     * (PREV_PREV_NOTE).
     *
     * @param request The request containing information about which previous
     *                version to restore, along with a unique
     *                identifier for the note that the restoration process concerns.
     * @return {@code true} if the note was successfully restored, {@code false}
     *         otherwise.
     * @throws SecurityException If the validation of the restore request fails.
     */
    Boolean restoreOldNote(RestoreNoteRequest request) throws SecurityException;
}
