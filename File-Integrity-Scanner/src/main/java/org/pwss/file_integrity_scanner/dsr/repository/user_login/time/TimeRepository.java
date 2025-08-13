package org.pwss.file_integrity_scanner.dsr.repository.user_login.time;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
}