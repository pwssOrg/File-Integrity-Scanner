package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.note;

/**
 * Enumeration representing different restoration options for notes.
 */
public enum RestoreNote {
    /**
     * Represents restoring to the previous note.
     *
     * This option indicates that the application should restore the note
     * to its last saved state before the current version.
     */
    PREV_NOTE,

    /**
     * Represents restoring to the prior-previous note.
     *
     * This option indicates that the application should restore the note
     * to its state two versions back from the current version.
     */
    PREV_PREV_NOTE;
}
