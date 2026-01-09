package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.ParaclinicalResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ParaclinicalResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParaclinicalResultRepository extends JpaRepository<ParaclinicalResult, Long> {}
