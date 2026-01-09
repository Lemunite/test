package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.PregnancyTetanus;
import com.mycompany.microservice.service.dto.PatientDTO;
import com.mycompany.microservice.service.dto.PregnancyTetanusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PregnancyTetanus} and its DTO {@link PregnancyTetanusDTO}.
 */
@Mapper(componentModel = "spring")
public interface PregnancyTetanusMapper extends EntityMapper<PregnancyTetanusDTO, PregnancyTetanus> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    PregnancyTetanusDTO toDto(PregnancyTetanus s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
