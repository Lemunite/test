package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.OrganExaminationAsserts.*;
import static com.mycompany.microservice.domain.OrganExaminationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganExaminationMapperTest {

    private OrganExaminationMapper organExaminationMapper;

    @BeforeEach
    void setUp() {
        organExaminationMapper = new OrganExaminationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganExaminationSample1();
        var actual = organExaminationMapper.toEntity(organExaminationMapper.toDto(expected));
        assertOrganExaminationAllPropertiesEquals(expected, actual);
    }
}
