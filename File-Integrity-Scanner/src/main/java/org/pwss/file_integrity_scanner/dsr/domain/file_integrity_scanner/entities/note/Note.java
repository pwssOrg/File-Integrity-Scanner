package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The Note entity represents a note record in the file integrity scanner
 * system.
 * It includes various versions of notes and is associated with a Time object
 * that
 * records when each version was created or modified.
 */
@Entity
@Table(name = "note")
public class Note extends PWSSbaseEntity {

    /**
     * Primary key for the Note entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Current notes content.
     */
    @Column(name = "notes")
    private String notes;

    /**
     * Previous version of the notes content.
     */
    @Column(name = "prev_notes")
    private String prevNotes;

    /**
     * Second previous version of the notes content.
     */
    @Column(name = "prev_prev_notes")
    private String prevPrevNotes;

    /**
     * Time object associated with this note, recording when each version was
     * created or modified.
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    /**
     * Default constructor for the Note entity.
     */
    public Note() {
    }

    /**
     * Constructor for creating a new note with initial notes content and associated
     * time.
     *
     * @param notes The current notes content
     * @param time  The time object associated with this note
     */
    public Note(String notes, Time time) {
        this.notes = notes;
        this.time = time;
    }

    /**
     * Retrieves the current notes content.
     *
     * @return The current notes content as a string
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the current notes content.
     *
     * @param notes The new current notes content to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Retrieves the previous version of the notes content.
     *
     * @return The previous notes content as a string
     */
    public String getPrevNotes() {
        return prevNotes;
    }

    /**
     * Sets the previous version of the notes content.
     *
     * @param prevNotes The new previous notes content to set
     */
    public void setPrevNotes(String prevNotes) {
        this.prevNotes = prevNotes;
    }

    /**
     * Retrieves the second previous version of the notes content.
     *
     * @return The second previous notes content as a string
     */
    public String getPrevPrevNotes() {
        return prevPrevNotes;
    }

    /**
     * Sets the second previous version of the notes content.
     *
     * @param prevPrevNotes The new second previous notes content to set
     */
    public void setPrevPrevNotes(String prevPrevNotes) {
        this.prevPrevNotes = prevPrevNotes;
    }

    /**
     * Retrieves the Time object associated with this note.
     *
     * @return The Time object
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the Time object associated with this note.
     *
     * @param time The new Time object to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Retrieves the primary key of this Note entity.
     *
     * @return The ID of this note as a Long
     */
    public Long getId() {
        return id;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }

}