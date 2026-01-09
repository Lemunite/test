package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.SurgeryHistory;
import com.mycompany.microservice.service.dto.PatientDTO;
import com.mycompany.microservice.service.dto.SurgeryHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SurgeryHistory} and its DTO {@link SurgeryHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SurgeryHistoryMapper extends EntityMapper<SurgeryHistoryDTO, SurgeryHistory> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    SurgeryHistoryDTO toDto(SurgeryHistory s);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);
}
