package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.PregnancyTetanus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PregnancyTetanus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PregnancyTetanusRepository extends JpaRepository<PregnancyTetanus, Long> {}
