package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.ParaclinicalResultAsserts.*;
import static com.mycompany.microservice.domain.ParaclinicalResultTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParaclinicalResultMapperTest {

    private ParaclinicalResultMapper paraclinicalResultMapper;

    @BeforeEach
    void setUp() {
        paraclinicalResultMapper = new ParaclinicalResultMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getParaclinicalResultSample1();
        var actual = paraclinicalResultMapper.toEntity(paraclinicalResultMapper.toDto(expected));
        assertParaclinicalResultAllPropertiesEquals(expected, actual);
    }
}
