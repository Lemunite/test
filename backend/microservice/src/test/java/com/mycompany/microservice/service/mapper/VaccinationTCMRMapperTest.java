package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.VaccinationTCMRAsserts.*;
import static com.mycompany.microservice.domain.VaccinationTCMRTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccinationTCMRMapperTest {

    private VaccinationTCMRMapper vaccinationTCMRMapper;

    @BeforeEach
    void setUp() {
        vaccinationTCMRMapper = new VaccinationTCMRMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVaccinationTCMRSample1();
        var actual = vaccinationTCMRMapper.toEntity(vaccinationTCMRMapper.toDto(expected));
        assertVaccinationTCMRAllPropertiesEquals(expected, actual);
    }
}
