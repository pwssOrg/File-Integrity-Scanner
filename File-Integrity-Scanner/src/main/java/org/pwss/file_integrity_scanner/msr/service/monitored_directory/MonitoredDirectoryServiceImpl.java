package org.pwss.file_integrity_scanner.msr.service.monitored_directory;

import org.pwss.file_integrity_scanner.msr.repository.MonitoredDirectoryRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class MonitoredDirectoryServiceImpl extends BaseService<MonitoredDirectoryRepository> implements MonitoredDirectoryService {

    public MonitoredDirectoryServiceImpl(MonitoredDirectoryRepository repository){
    super(repository);
}
    
}