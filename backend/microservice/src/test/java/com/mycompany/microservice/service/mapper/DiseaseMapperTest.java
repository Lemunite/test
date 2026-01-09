package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.DiseaseAsserts.*;
import static com.mycompany.microservice.domain.DiseaseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiseaseMapperTest {

    private DiseaseMapper diseaseMapper;

    @BeforeEach
    void setUp() {
        diseaseMapper = new DiseaseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDiseaseSample1();
        var actual = diseaseMapper.toEntity(diseaseMapper.toDto(expected));
        assertDiseaseAllPropertiesEquals(expected, actual);
    }
}
