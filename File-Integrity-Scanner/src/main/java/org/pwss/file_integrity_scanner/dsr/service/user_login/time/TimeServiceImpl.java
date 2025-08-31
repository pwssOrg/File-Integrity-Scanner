package org.pwss.file_integrity_scanner.dsr.service.user_login.time;

import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.user_login.time.TimeRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing time-related operations related to PWSS Users.
 *
 * This class provides methods to save {@code Time} objects. It extends
 * {@code BaseService}, which contains common functionality shared among
 * service implementations.
 */
@Service
public class TimeServiceImpl extends PWSSbaseService<TimeRepository,Time, Long> implements TimeService {

   /**
     * Constructs a new instance of TimeServiceImpl with the specified repository.
     *
     * @param repository The repository used to perform CRUD operations on time data.
     */
    public TimeServiceImpl(TimeRepository repository) {
        super(repository);
    }

    @Override
    public Time save(Time time) {
      return repository.save(time);
    }
}
