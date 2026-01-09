package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.FamilyDisease;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FamilyDisease entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyDiseaseRepository extends JpaRepository<FamilyDisease, Long> {}
