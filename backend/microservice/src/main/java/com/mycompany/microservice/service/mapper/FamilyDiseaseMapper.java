package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.FamilyDisease;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.service.dto.FamilyDiseaseDTO;
import com.mycompany.microservice.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FamilyDisease} and its DTO {@link FamilyDiseaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface FamilyDiseaseMapper extends EntityMapper<FamilyDiseaseDTO, FamilyDisease> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    FamilyDiseaseDTO toDto(FamilyDisease s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
