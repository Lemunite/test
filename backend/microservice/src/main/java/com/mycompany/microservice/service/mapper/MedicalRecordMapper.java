package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.MedicalRecord;
import com.mycompany.microservice.domain.OrganExamination;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.service.dto.MedicalRecordDTO;
import com.mycompany.microservice.service.dto.OrganExaminationDTO;
import com.mycompany.microservice.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalRecord} and its DTO {@link MedicalRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalRecordMapper extends EntityMapper<MedicalRecordDTO, MedicalRecord> {
    @Mapping(target = "organExamination", source = "organExamination", qualifiedByName = "organExaminationId")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    MedicalRecordDTO toDto(MedicalRecord s);

    @Named("organExaminationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganExaminationDTO toDtoOrganExaminationId(OrganExamination organExamination);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
