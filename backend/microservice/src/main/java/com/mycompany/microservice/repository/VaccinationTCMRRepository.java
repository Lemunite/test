package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.VaccinationTCMR;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VaccinationTCMR entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationTCMRRepository extends JpaRepository<VaccinationTCMR, Long> {}
