package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.SurgeryHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SurgeryHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurgeryHistoryRepository extends JpaRepository<SurgeryHistory, Long> {}
