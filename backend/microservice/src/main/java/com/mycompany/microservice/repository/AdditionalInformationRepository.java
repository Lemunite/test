package com.mycompany.microservice.repository;

import com.mycompany.microservice.domain.AdditionalInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdditionalInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdditionalInformationRepository extends JpaRepository<AdditionalInformation, Long> {}
