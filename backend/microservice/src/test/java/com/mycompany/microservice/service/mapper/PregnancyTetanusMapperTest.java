package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.PregnancyTetanusAsserts.*;
import static com.mycompany.microservice.domain.PregnancyTetanusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PregnancyTetanusMapperTest {

    private PregnancyTetanusMapper pregnancyTetanusMapper;

    @BeforeEach
    void setUp() {
        pregnancyTetanusMapper = new PregnancyTetanusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPregnancyTetanusSample1();
        var actual = pregnancyTetanusMapper.toEntity(pregnancyTetanusMapper.toDto(expected));
        assertPregnancyTetanusAllPropertiesEquals(expected, actual);
    }
}
