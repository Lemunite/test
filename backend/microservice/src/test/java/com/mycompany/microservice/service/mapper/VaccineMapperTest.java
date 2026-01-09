package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.VaccineAsserts.*;
import static com.mycompany.microservice.domain.VaccineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccineMapperTest {

    private VaccineMapper vaccineMapper;

    @BeforeEach
    void setUp() {
        vaccineMapper = new VaccineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVaccineSample1();
        var actual = vaccineMapper.toEntity(vaccineMapper.toDto(expected));
        assertVaccineAllPropertiesEquals(expected, actual);
    }
}
