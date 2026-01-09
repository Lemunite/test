package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.FamilyAllergy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FamilyAllergy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyAllergyRepository extends JpaRepository<FamilyAllergy, Long> {}
