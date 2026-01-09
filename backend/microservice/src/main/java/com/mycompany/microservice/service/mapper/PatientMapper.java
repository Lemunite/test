package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.AdditionalInformation;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.VaccinationForBaby;
import com.mycompany.microservice.service.dto.AdditionalInformationDTO;
import com.mycompany.microservice.service.dto.PatientDTO;
import com.mycompany.microservice.service.dto.VaccinationForBabyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring")
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {
    @Mapping(target = "additionalInfo", source = "additionalInfo", qualifiedByName = "additionalInformationId")
    @Mapping(target = "vaccinationsForBaby", source = "vaccinationsForBaby", qualifiedByName = "vaccinationForBabyId")
    PatientDTO toDto(Patient s);

    @Named("additionalInformationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdditionalInformationDTO toDtoAdditionalInformationId(AdditionalInformation additionalInformation);

    @Named("vaccinationForBabyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationForBabyDTO toDtoVaccinationForBabyId(VaccinationForBaby vaccinationForBaby);
}
