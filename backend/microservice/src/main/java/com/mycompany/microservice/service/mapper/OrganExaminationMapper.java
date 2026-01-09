package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.OrganExamination;
import com.mycompany.microservice.service.dto.OrganExaminationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganExamination} and its DTO {@link OrganExaminationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganExaminationMapper extends EntityMapper<OrganExaminationDTO, OrganExamination> {}
