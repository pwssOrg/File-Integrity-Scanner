package org.pwss.file_integrity_scanner.dsr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<Repository extends JpaRepository> {
    protected final Repository repository;

    @Autowired
    public BaseService(Repository repository) {
        this.repository = repository;
    }
}
