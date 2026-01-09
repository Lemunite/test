package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.VaccinationTCMR;
import com.mycompany.microservice.service.dto.PatientDTO;
import com.mycompany.microservice.service.dto.VaccinationTCMRDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VaccinationTCMR} and its DTO {@link VaccinationTCMRDTO}.
 */
@Mapper(componentModel = "spring")
public interface VaccinationTCMRMapper extends EntityMapper<VaccinationTCMRDTO, VaccinationTCMR> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    VaccinationTCMRDTO toDto(VaccinationTCMR s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
