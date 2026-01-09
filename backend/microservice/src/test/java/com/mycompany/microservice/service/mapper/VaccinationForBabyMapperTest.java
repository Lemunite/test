package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.VaccinationForBabyAsserts.*;
import static com.mycompany.microservice.domain.VaccinationForBabyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccinationForBabyMapperTest {

    private VaccinationForBabyMapper vaccinationForBabyMapper;

    @BeforeEach
    void setUp() {
        vaccinationForBabyMapper = new VaccinationForBabyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVaccinationForBabySample1();
        var actual = vaccinationForBabyMapper.toEntity(vaccinationForBabyMapper.toDto(expected));
        assertVaccinationForBabyAllPropertiesEquals(expected, actual);
    }
}
