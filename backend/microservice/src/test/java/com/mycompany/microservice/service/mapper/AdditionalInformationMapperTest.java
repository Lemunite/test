package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.AdditionalInformationAsserts.*;
import static com.mycompany.microservice.domain.AdditionalInformationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdditionalInformationMapperTest {

    private AdditionalInformationMapper additionalInformationMapper;

    @BeforeEach
    void setUp() {
        additionalInformationMapper = new AdditionalInformationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdditionalInformationSample1();
        var actual = additionalInformationMapper.toEntity(additionalInformationMapper.toDto(expected));
        assertAdditionalInformationAllPropertiesEquals(expected, actual);
    }
}
