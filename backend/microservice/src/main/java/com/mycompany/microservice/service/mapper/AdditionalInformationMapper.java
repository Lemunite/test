package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.AdditionalInformation;
import com.mycompany.microservice.service.dto.AdditionalInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdditionalInformation} and its DTO {@link AdditionalInformationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdditionalInformationMapper extends EntityMapper<AdditionalInformationDTO, AdditionalInformation> {}
