package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.FamilyAllergy;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.service.dto.FamilyAllergyDTO;
import com.mycompany.microservice.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FamilyAllergy} and its DTO {@link FamilyAllergyDTO}.
 */
@Mapper(componentModel = "spring")
public interface FamilyAllergyMapper extends EntityMapper<FamilyAllergyDTO, FamilyAllergy> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    FamilyAllergyDTO toDto(FamilyAllergy s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
