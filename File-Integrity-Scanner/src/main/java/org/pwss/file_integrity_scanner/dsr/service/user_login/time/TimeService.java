package org.pwss.file_integrity_scanner.dsr.service.user_login.time;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;

public interface TimeService {

   
    /**
     * Saves the given time object to the repository and returns the saved entity.
     *
     * @param time The time object to be saved. Must not be null.
     * @return The saved {@code Time} entity.
     */
    Time save(Time time);
}
