package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.note.RestoreNote;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A data class representing a request to restore a previous note.
 */
@Schema(description = "Represents a request to restore a previous note")
public record RestoreNoteRequest(
        /**
         * The unique identifier of the note object that contains all notes
         */
        @Schema(description = "The unique ID of the note object", example = "12345") Long noteId,

         /**
         * An enum reference for the note to restore.
         */
        @Schema(description = "The type of previous note to restore (PREV_NOTE or PREV_PREV_NOTE)", example = "PREV_NOTE") RestoreNote restoreNote) {
}