package com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.SurgeryHistoryAsserts.*;
import static com.mycompany.microservice.domain.SurgeryHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SurgeryHistoryMapperTest {

    private SurgeryHistoryMapper surgeryHistoryMapper;

    @BeforeEach
    void setUp() {
        surgeryHistoryMapper = new SurgeryHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSurgeryHistorySample1();
        var actual = surgeryHistoryMapper.toEntity(surgeryHistoryMapper.toDto(expected));
        assertSurgeryHistoryAllPropertiesEquals(expected, actual);
    }
}
