package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.FamilyDiseaseAsserts.*;
import static com.mycompany.microservice.domain.FamilyDiseaseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamilyDiseaseMapperTest {

    private FamilyDiseaseMapper familyDiseaseMapper;

    @BeforeEach
    void setUp() {
        familyDiseaseMapper = new FamilyDiseaseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFamilyDiseaseSample1();
        var actual = familyDiseaseMapper.toEntity(familyDiseaseMapper.toDto(expected));
        assertFamilyDiseaseAllPropertiesEquals(expected, actual);
    }
}
