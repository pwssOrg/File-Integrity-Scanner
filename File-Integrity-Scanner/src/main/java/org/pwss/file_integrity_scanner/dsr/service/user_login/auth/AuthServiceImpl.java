package org.pwss.file_integrity_scanner.dsr.service.user_login.auth;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.pwss.file_integrity_scanner.dsr.repository.user_login.auth.AuthRepository;
import org.pwss.file_integrity_scanner.dsr.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing authentication-related operations.
 *
 * This class provides methods to save authentication information and check if
 * the repository is empty. It extends {@code BaseService} which likely contains
 * common functionality shared among service implementations.
 */
@Service
public class AuthServiceImpl extends BaseService<AuthRepository> implements AuthService {

    /**
     * Constructs a new instance of AuthServiceImpl with the specified repository.
     *
     * @param repository The repository used to perform CRUD operations on
     *                   authentication data.
     */
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
