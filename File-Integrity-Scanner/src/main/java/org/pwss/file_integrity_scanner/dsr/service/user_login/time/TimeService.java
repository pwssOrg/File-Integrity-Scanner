package org.pwss.file_integrity_scanner.dsr.service.user_login.time;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;

public interface TimeService {

    /**
     * Saves a Time entity to the database.
     *
     * @param time the Time entity to save
     */
    Time save(Time time);
}
