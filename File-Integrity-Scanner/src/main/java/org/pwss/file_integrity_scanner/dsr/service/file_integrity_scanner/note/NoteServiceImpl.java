package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;

import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.NoteRepository;

import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;

import org.pwss.file_integrity_scanner.dsr.service.user_login.time.TimeServiceImpl;
import org.springframework.stereotype.Service;

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
}
