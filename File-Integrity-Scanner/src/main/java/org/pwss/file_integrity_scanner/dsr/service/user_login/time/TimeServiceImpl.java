package org.pwss.file_integrity_scanner.dsr.service.user_login.time;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.user_login.time.TimeRepository;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl extends BaseService<TimeRepository> implements TimeService {

    @Autowired
    public TimeServiceImpl(TimeRepository repository) {
        super(repository);
    }

    @Override
    public Time save(Time time) {
      return repository.save(time);
    }
}
