package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.VaccinationForBaby;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VaccinationForBaby entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationForBabyRepository extends JpaRepository<VaccinationForBaby, Long> {}
