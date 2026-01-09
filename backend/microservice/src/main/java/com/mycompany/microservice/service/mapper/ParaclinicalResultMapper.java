package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.MedicalRecord;
import com.mycompany.microservice.domain.ParaclinicalResult;
import com.mycompany.microservice.service.dto.MedicalRecordDTO;
import com.mycompany.microservice.service.dto.ParaclinicalResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParaclinicalResult} and its DTO {@link ParaclinicalResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParaclinicalResultMapper extends EntityMapper<ParaclinicalResultDTO, ParaclinicalResult> {
    @Mapping(target = "medicalRecord", source = "medicalRecord", qualifiedByName = "medicalRecordId")
    ParaclinicalResultDTO toDto(ParaclinicalResult s);

    @Named("medicalRecordId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicalRecordDTO toDtoMedicalRecordId(MedicalRecord medicalRecord);
}
