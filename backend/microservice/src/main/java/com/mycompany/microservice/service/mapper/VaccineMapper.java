package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.VaccinationForBaby;
import com.mycompany.microservice.domain.Vaccine;
import com.mycompany.microservice.service.dto.VaccinationForBabyDTO;
import com.mycompany.microservice.service.dto.VaccineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vaccine} and its DTO {@link VaccineDTO}.
 */
@Mapper(componentModel = "spring")
public interface VaccineMapper extends EntityMapper<VaccineDTO, Vaccine> {
    @Mapping(target = "vaccinationForBaby", source = "vaccinationForBaby", qualifiedByName = "vaccinationForBabyId")
    VaccineDTO toDto(Vaccine s);

    @Named("vaccinationForBabyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationForBabyDTO toDtoVaccinationForBabyId(VaccinationForBaby vaccinationForBaby);
}
