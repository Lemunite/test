package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.OrganExamination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrganExamination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganExaminationRepository extends JpaRepository<OrganExamination, Long> {}
