package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.AllergyAsserts.*;
import static com.mycompany.microservice.domain.AllergyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllergyMapperTest {

    private AllergyMapper allergyMapper;

    @BeforeEach
    void setUp() {
        allergyMapper = new AllergyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAllergySample1();
        var actual = allergyMapper.toEntity(allergyMapper.toDto(expected));
        assertAllergyAllPropertiesEquals(expected, actual);
    }
}
