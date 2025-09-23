package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.note.RestoreNote;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller.RestoreNoteRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller.UpdateNoteRequest;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.note.NoteRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;

import org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeServiceImpl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * Service implementation for managing {@link Note} entities.
 *
 * This service provides methods to perform CRUD operations on notes,
 * including creating, updating, and retrieving note information from the
 * repository layer.
 */
@Service
public class NoteServiceImpl extends PWSSbaseService<NoteRepository, Note, Long> implements NoteService {

    /**
     * Logger for logging messages related to this service.
     */
    private final org.slf4j.Logger log;

    /**
     * Service for handling time-related operations associated with notes.
     */
    private final TimeServiceImpl timeService;

    /**
     * Constructs a new instance of the {@link NoteServiceImpl} class.
     *
     * @param repository  the repository used to perform data access operations on
     *                    note entities
     * @param timeService the service for handling time-related operations
     *                    associated with notes
     */
    public NoteServiceImpl(NoteRepository repository,
            TimeServiceImpl timeService) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(NoteServiceImpl.class);
        this.timeService = timeService;

    }

    @Override
    public void save(Note note) {

        this.repository.save(note);

    }

    @Override
    public Boolean updateNote(UpdateNoteRequest request) throws SecurityException {

        if (validateRequest(request)) {
            java.util.Optional<Note> oNote = this.repository.findById(request.noteId());

            if (oNote.isPresent()) {

                return updateNote(oNote.get(), request.text());
            }

        }

        else {
            throw new SecurityException("Validation of the request object failed");
        }

        return false;
    }

    @Transactional
    @Override
    public Boolean updateNote(Note note, String noteText) {

        boolean doesExistsInRepositoryLayer;

        try {
            // Check if the note already exists in the repository layer by verifying its ID
            doesExistsInRepositoryLayer = note.getId() > 0;
        } catch (NullPointerException nullPointerException) {
            log.debug(
                    "No ID exists for the note that you are trying to update. It is most likely not present in the repository layer!");
            doesExistsInRepositoryLayer = false;
        }

        if (!doesExistsInRepositoryLayer) {
            // Return early if the note doesn't exist
            return false;
        }

        String firstNote = null;

        try {
            // Attempt to get the current notes text
            firstNote = note.getNotes();
        } catch (NullPointerException nullPointerException) {
            log.debug("First note is null");
        }

        if (firstNote == null || firstNote.isEmpty()) {
            note.setNotes(noteText);
            updateAndSaveTime(note);
            this.repository.save(note);
            return true;
        }

        String secondNote = null;

        try {
            // Attempt to get the previous notes text
            secondNote = note.getPrevNotes();
        } catch (NullPointerException nullPointerException) {
            log.debug("Second note is null");
        }

        if (secondNote == null || secondNote.isEmpty()) {
            note.setPrevNotes(note.getNotes());
            note.setNotes(noteText);
            updateAndSaveTime(note);
            this.repository.save(note);
            return true;
        }

        String thirdNote = null;

        try {
            // Attempt to get the previous previous notes text
            thirdNote = note.getPrevPrevNotes();
        } catch (NullPointerException nullPointerException) {
            log.debug("Third note is null");
        }

        if (thirdNote == null || thirdNote.isEmpty()) {

            note.setPrevPrevNotes(note.getPrevNotes());
            note.setPrevNotes(note.getNotes());
            note.setNotes(noteText);
            updateAndSaveTime(note);
            this.repository.save(note);
            return true;
        }

        // All notes have content
        final String originalFirstNote = note.getNotes();
        final String originalSecondNote = note.getPrevNotes();
        final String originalThirdNote = note.getPrevPrevNotes();

        note.setNotes(noteText);
        note.setPrevNotes(originalFirstNote);
        note.setPrevPrevNotes(originalSecondNote);

        log.debug("Note with value - {}\n will be removed from the repository layer", originalThirdNote);

        updateAndSaveTime(note);
        this.repository.save(note);
        return true;
    }

    /**
     * Updates and saves the time information associated with a Note.
     *
     * This method checks the Time object which is associated with the given Note.
     * It updates the 'updated' field of the Time object to the current
     * timestamp
     * and then saves this updated Time object using the TimeService.
     *
     * @param note the {@link Note} for which the time information should be
     *             updated
     */
    private void updateAndSaveTime(Note note) {

        Time time = null;

        try {

            time = note.getTime();
        }

        catch (NullPointerException nullPointerException) {

            throw new NullPointerException("The time information associated with a Note shall never be null!");
        }

        time.setUpdated(OffsetDateTime.now());
        timeService.save(time);

    }

    @Override
    public Boolean anyNoteContains(Note note, String text) {

        String firstNote = "";
        String secondNote = "";
        String thirdNote = "";

        try {

            firstNote = note.getNotes();
        }

        catch (NullPointerException nullPointerException) {

            log.debug("First note is null");
        }

        try {

            secondNote = note.getPrevNotes();
        }

        catch (NullPointerException nullPointerException) {

            log.debug("Second note is null");
        }

        try {

            thirdNote = note.getPrevPrevNotes();
        }

        catch (NullPointerException nullPointerException) {

            log.debug("Third Note is null");
        }

        if (firstNote.contains(text) || secondNote.contains(text) || thirdNote.contains(text))
            return true;
        else
            return false;

    }

    @Transactional
    @Override
    public Boolean restoreOldNote(RestoreNoteRequest request) throws SecurityException {

        if (validateRequest(request)) {

            boolean doesExistsInRepositoryLayer;

            log.debug("Will try to fetch note by ID");
            java.util.Optional<Note> oNote = this.repository.findById(request.noteId());

            if (oNote.isPresent()) {

                log.debug("Note is present in the repository layer");

                Note note = oNote.get();

                try {
                    // Check if the note already exists in the repository layer by verifying its ID
                    doesExistsInRepositoryLayer = note.getId() > 0;
                } catch (NullPointerException nullPointerException) {
                    log.debug(
                            "Please note: No ID exists for the note that you are trying to initiate a restore process on");
                    doesExistsInRepositoryLayer = false;
                }

                if (!doesExistsInRepositoryLayer) {
                    // Return early if the note doesn't exist
                    return false;

                }

                final RestoreNote restoreNote = request.restoreNote();

                switch (restoreNote) {
                    case PREV_NOTE:
                        return restorePrevNote(note);
                    case PREV_PREV_NOTE:
                        return restorePrevPrevNote(note);
                    default:
                        throw new IllegalArgumentException("Restore note argument was not valid");
                }
            }

            else {

                throw new NoSuchElementException(
                        "No ID exists for the note that you are trying to initiate a restore process on");
            }

        }

        else {
            throw new SecurityException("Validation Failed");
        }

    }

    /**
     * Restores the previous version of notes for a given note.
     *
     * This method attempts to restore the primary notes from the "previous notes"
     * field.
     * It logs debug messages indicating each step, sets the current primary notes
     * into
     * the "previous notes" field, and then updates the primary notes with the
     * content
     * of the previous notes. Finally, it saves the updated note using the
     * repository.
     *
     * @param note The note to be restored. Must not be null.
     * @return {@code true} if the restoration was successful; {@code false}
     *         otherwise.
     */
    private Boolean restorePrevNote(Note note) {

        String noteToRestore = "";

        try {

            noteToRestore = note.getPrevNotes();

            log.debug("Setting current primary notes into prev notes");
            note.setPrevNotes(note.getNotes());

            log.debug("Setting old previous notes into primary notes");
            note.setNotes(noteToRestore);

            updateAndSaveTime(note);
            this.repository.save(note);

            return true;

        }

        catch (NullPointerException nullPointerException) {

            log.debug("1st or 2nd note has null value");
            return false;
        }

    }

    /**
     * Restores the previous-previous version of notes for a given note.
     *
     * This method attempts to restore the primary notes from the "previous-previous
     * notes" field.
     * It logs debug messages indicating each step, sets the current primary notes
     * into
     * the "previous-previous notes" field, and then updates the primary notes with
     * the content
     * of the previous-previous notes. Finally, it saves the updated note using the
     * repository.
     *
     * @param note The note to be restored. Must not be null.
     * @return {@code true} if the restoration was successful; {@code false}
     *         otherwise.
     */
    private Boolean restorePrevPrevNote(Note note) {

        String noteToRestore = "";

        try {

            noteToRestore = note.getPrevPrevNotes();

            log.debug("Setting current primary notes into prevprev notes");
            note.setPrevPrevNotes(note.getNotes());

            log.debug("Setting old previous-previous notes into primary notes");
            note.setNotes(noteToRestore);

            updateAndSaveTime(note);
            this.repository.save(note);

            return true;

        }

        catch (NullPointerException nullPointerException) {

            log.debug("1st or 3rd note has null value");
            return false;
        }

    }

}
