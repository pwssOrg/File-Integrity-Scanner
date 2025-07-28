package org.pwss.file_integrity_scanner.msr.service.scan;
import org.pwss.file_integrity_scanner.msr.repository.ScanRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ScanServiceImpl extends BaseService<ScanRepository> implements ScanService {

    public ScanServiceImpl(ScanRepository repository){
    super(repository);
}
    
}
