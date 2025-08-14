package org.pwss.file_integrity_scanner.dsr.service.user_login.auth;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.pwss.file_integrity_scanner.dsr.repository.user_login.auth.AuthRepository;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl extends BaseService<AuthRepository> implements AuthService {

    @Autowired
    public AuthServiceImpl(AuthRepository repository) {
        super(repository);
    }

    @Override
    public void save(Auth auth) {
        repository.save(auth);
    }

    @Override
    public Boolean isEmpty() {
        return repository.count() == (0L);
    }
}
