package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.note.NoteRepository;
import org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeServiceImpl;

public class NoteServiceTest {

    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private NoteRepository repository;

    @Mock
    private TimeServiceImpl timeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Set the ID using reflection since we have no setter or constructor that can
    // set the ID.
    private void setId(Note note, Long id) throws NoSuchFieldException, IllegalAccessException {
        Field idField = Note.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(note, id);
    }

    private Time createAssociatedTimeObject() {

        return new Time(OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Test
    public void testUpdateNote_whenNoteDoesNotExist() throws NoSuchFieldException, IllegalAccessException {
        // Setup a note with no ID (not present in repository)
        Note note = new Note();
        setId(note, null);
        note.setTime(createAssociatedTimeObject());

        Boolean result = noteService.updateNote(note, "New note text");

        assertFalse(result);
    }

    @Test
    public void testUpdateNote_whenFirstNoteIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        // Setup a note with an empty first note
        Note note = new Note();
        setId(note, 1L); // Set an ID to simulate it exists in the repository
        note.setTime(createAssociatedTimeObject());

        Boolean result = noteService.updateNote(note, "New note text");

        assertTrue(result);
    }

    @Test
    public void testUpdateNote_whenSecondNoteIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        Note note = new Note();
        setId(note, 1L); // Set an ID to simulate it exists in the repository
        note.setTime(createAssociatedTimeObject());
        note.setNotes("First note content");

        Boolean result = noteService.updateNote(note, "New note text");

        assertTrue(result);
    }

    @Test
    public void testUpdateNote_whenAllNotesHaveContent() throws NoSuchFieldException, IllegalAccessException {
        Note note = new Note();
        setId(note, 1L); // Set an ID to simulate it exists in the repository
        note.setTime(createAssociatedTimeObject());
        note.setNotes("First note content");
        note.setPrevNotes("Second note content");
        note.setPrevPrevNotes("Third note content");

        Boolean result = noteService.updateNote(note, "New note text");

        assertTrue(result);

        verify(repository, times(1)).save(any(Note.class));
    }

}
