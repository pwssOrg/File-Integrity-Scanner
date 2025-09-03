package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;
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
    public NoteServiceImpl(NoteRepository repository, TimeServiceImpl timeService) {
        super(repository);
        this.log = org.slf4j.LoggerFactory.getLogger(NoteServiceImpl.class);
        this.timeService = timeService;
    }

    @Override
    public void save(Note note) {

        this.repository.save(note);

    }

    @Transactional
    @Override
    public Boolean updateNote(Note note, String noteText) {

        boolean doesExistsInRepositoryLayer;

        try {
            // Check if the note already exists in the repository layer by verifying its ID
            doesExistsInRepositoryLayer = note.getId() > 0;
        } catch (NullPointerException nullPointerException) {
            log.error(
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
            note.setPrevNotes(noteText);
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
            note.setPrevPrevNotes(noteText);
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
        String thirddNote = "";

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

            thirddNote = note.getPrevPrevNotes();
        }

        catch (NullPointerException nullPointerException) {

            log.debug("Third Note is null");
        }

        if (firstNote.contains(text) || secondNote.contains(text) || thirddNote.contains(text))
            return true;
        else
            return false;

    }

}
