package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.DisabilityAsserts.*;
import static com.mycompany.microservice.domain.DisabilityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisabilityMapperTest {

    private DisabilityMapper disabilityMapper;

    @BeforeEach
    void setUp() {
        disabilityMapper = new DisabilityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDisabilitySample1();
        var actual = disabilityMapper.toEntity(disabilityMapper.toDto(expected));
        assertDisabilityAllPropertiesEquals(expected, actual);
    }
}
