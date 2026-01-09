package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PregnancyTetanusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PregnancyTetanusDTO.class);
        PregnancyTetanusDTO pregnancyTetanusDTO1 = new PregnancyTetanusDTO();
        pregnancyTetanusDTO1.setId(1L);
        PregnancyTetanusDTO pregnancyTetanusDTO2 = new PregnancyTetanusDTO();
        assertThat(pregnancyTetanusDTO1).isNotEqualTo(pregnancyTetanusDTO2);
        pregnancyTetanusDTO2.setId(pregnancyTetanusDTO1.getId());
        assertThat(pregnancyTetanusDTO1).isEqualTo(pregnancyTetanusDTO2);
        pregnancyTetanusDTO2.setId(2L);
        assertThat(pregnancyTetanusDTO1).isNotEqualTo(pregnancyTetanusDTO2);
        pregnancyTetanusDTO1.setId(null);
        assertThat(pregnancyTetanusDTO1).isNotEqualTo(pregnancyTetanusDTO2);
    }
}
