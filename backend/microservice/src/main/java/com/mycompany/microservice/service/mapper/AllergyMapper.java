package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.Allergy;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.service.dto.AllergyDTO;
import com.mycompany.microservice.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Allergy} and its DTO {@link AllergyDTO}.
 */
@Mapper(componentModel = "spring")
public interface AllergyMapper extends EntityMapper<AllergyDTO, Allergy> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    AllergyDTO toDto(Allergy s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
