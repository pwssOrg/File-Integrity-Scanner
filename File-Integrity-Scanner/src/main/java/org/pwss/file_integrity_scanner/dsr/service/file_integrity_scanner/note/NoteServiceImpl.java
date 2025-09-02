package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.note.NoteRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeServiceImpl;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class NoteServiceImpl extends PWSSbaseService<NoteRepository, Note, Long> implements NoteService {

    private final org.slf4j.Logger log;

    private final TimeServiceImpl timeService;

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

            doesExistsInRepositoryLayer = note.getId() > 0;
        }

        catch (NullPointerException nullPointerException) {

            log.error(
                    "No ID exists for the note that you are trying to update. It is most likely not present in the repository layer!");

            doesExistsInRepositoryLayer = false;

        }

        if (doesExistsInRepositoryLayer) {

            String firstNote = null;

            try {

                firstNote = note.getNotes();
            }

            catch (NullPointerException nullPointerException) {

                log.debug("First note is null");
            }

            if (firstNote != null && !firstNote.isEmpty()) {

                String secondNote = null;

                try {

                    secondNote = note.getPrevNotes();
                }

                catch (NullPointerException nullPointerException) {

                    log.debug("second note is null");
                }

                if (secondNote != null && !secondNote.isEmpty()) {

                    String thirdNote = null;

                    try {

                        thirdNote = note.getPrevPrevNotes();
                    }

                    catch (NullPointerException nullPointerException) {

                        log.debug("Third note is null");
                    }

                    if (thirdNote != null && !thirdNote.isEmpty()) {
                        log.debug("All notes have content!");
                        log.debug(
                                "Will now move all notes one step down (3th note will be removed) and set the update text to the firstNote variable");

                        final String originalFirstNote = note.getNotes();
                        final String originalSecondNote = note.getPrevNotes();
                        final String originalThirdNote = note.getPrevPrevNotes();

                        note.setNotes(noteText);
                        note.setPrevNotes(originalFirstNote);
                        note.setPrevPrevNotes(originalSecondNote);

                        log.debug("Note with value - {}\n will be removed from the repository layer",
                                originalThirdNote);

                        Time time = note.getTime();

                        time.setUpdated(OffsetDateTime.now());

                        timeService.save(time);
                        this.repository.save(note);
                        return true;

                    }

                    else if (thirdNote == null || thirdNote.isEmpty()) {
                        note.setPrevPrevNotes(noteText);
                        return true;
                    }
                }

                else if (secondNote == null || secondNote.isEmpty()) {

                    note.setPrevNotes(noteText);
                    return true;
                }

            } else if (firstNote == null || firstNote.isEmpty()) {

                note.setNotes(noteText);
                return true;

            }

        }

        return false;
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
