package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entity class representing notes in file integrity scanner entites.
 */
@Entity
@Table(name = "note")
public class Note extends PWSSbaseEntity {

    // TODO: Add Java Docs for entire class

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notes")
    private String notes;

    @Column(name = "prev_notes")
    private String prevNotes;

    @Column(name = "prev_prev_notes")
    private String prevPrevNotes;

    @OneToOne(optional = false)
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    public Note() {
    }

    public Note(String notes, Time time) {
        this.notes = notes;
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrevNotes() {
        return prevNotes;
    }

    public void setPrevNotes(String prevNotes) {
        this.prevNotes = prevNotes;
    }

    public String getPrevPrevNotes() {
        return prevPrevNotes;
    }

    public void setPrevPrevNotes(String prevPrevNotes) {
        this.prevPrevNotes = prevPrevNotes;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }

}