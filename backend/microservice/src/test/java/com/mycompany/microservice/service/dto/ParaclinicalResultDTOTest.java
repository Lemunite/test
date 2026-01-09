package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParaclinicalResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParaclinicalResultDTO.class);
        ParaclinicalResultDTO paraclinicalResultDTO1 = new ParaclinicalResultDTO();
        paraclinicalResultDTO1.setId(1L);
        ParaclinicalResultDTO paraclinicalResultDTO2 = new ParaclinicalResultDTO();
        assertThat(paraclinicalResultDTO1).isNotEqualTo(paraclinicalResultDTO2);
        paraclinicalResultDTO2.setId(paraclinicalResultDTO1.getId());
        assertThat(paraclinicalResultDTO1).isEqualTo(paraclinicalResultDTO2);
        paraclinicalResultDTO2.setId(2L);
        assertThat(paraclinicalResultDTO1).isNotEqualTo(paraclinicalResultDTO2);
        paraclinicalResultDTO1.setId(null);
        assertThat(paraclinicalResultDTO1).isNotEqualTo(paraclinicalResultDTO2);
    }
}
