package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A data class representing a request to update a note.
 */
@Schema(description = "Represents a request to update an existing note.")
public record UpdateNoteRequest(
        /**
         * The unique identifier of the note to be updated.
         */
        @Schema(description = "The unique ID of the note", example = "12345") Long noteId,

        /**
         * The new text content for the note.
         */
        @Schema(description = "The text content of the note", example = "This is an updated note.") String text) {
}