package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.MedicalRecordAsserts.*;
import static com.mycompany.microservice.domain.MedicalRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalRecordMapperTest {

    private MedicalRecordMapper medicalRecordMapper;

    @BeforeEach
    void setUp() {
        medicalRecordMapper = new MedicalRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalRecordSample1();
        var actual = medicalRecordMapper.toEntity(medicalRecordMapper.toDto(expected));
        assertMedicalRecordAllPropertiesEquals(expected, actual);
    }
}
