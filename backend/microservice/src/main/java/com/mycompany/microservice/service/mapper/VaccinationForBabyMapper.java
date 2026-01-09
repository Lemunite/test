package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.VaccinationForBaby;
import com.mycompany.microservice.service.dto.VaccinationForBabyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VaccinationForBaby} and its DTO {@link VaccinationForBabyDTO}.
 */
@Mapper(componentModel = "spring")
public interface VaccinationForBabyMapper extends EntityMapper<VaccinationForBabyDTO, VaccinationForBaby> {}
