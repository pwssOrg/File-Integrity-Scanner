package org.pwss.file_integrity_scanner.msr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<Repository extends JpaRepository> {
    protected final Repository repository;

    public BaseService(@Autowired Repository repository) {
        this.repository = repository;
    }
}
