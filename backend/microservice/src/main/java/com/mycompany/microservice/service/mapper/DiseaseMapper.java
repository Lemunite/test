package com.mycompany.microservice.service.mapper;

import com.mycompany.microservice.domain.Disease;
import com.mycompany.microservice.domain.FamilyDisease;
import com.mycompany.microservice.service.dto.DiseaseDTO;
import com.mycompany.microservice.service.dto.FamilyDiseaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disease} and its DTO {@link DiseaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiseaseMapper extends EntityMapper<DiseaseDTO, Disease> {
    @Mapping(target = "familyDisease", source = "familyDisease", qualifiedByName = "familyDiseaseId")
    DiseaseDTO toDto(Disease s);

    @Named("familyDiseaseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FamilyDiseaseDTO toDtoFamilyDiseaseId(FamilyDisease familyDisease);
}
