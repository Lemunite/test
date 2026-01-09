package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.Disability;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.service.dto.DisabilityDTO;
import com.mycompany.microservice.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disability} and its DTO {@link DisabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisabilityMapper extends EntityMapper<DisabilityDTO, Disability> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    DisabilityDTO toDto(Disability s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
