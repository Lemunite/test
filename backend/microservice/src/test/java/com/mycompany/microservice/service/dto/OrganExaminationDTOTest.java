package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganExaminationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganExaminationDTO.class);
        OrganExaminationDTO organExaminationDTO1 = new OrganExaminationDTO();
        organExaminationDTO1.setId(1L);
        OrganExaminationDTO organExaminationDTO2 = new OrganExaminationDTO();
        assertThat(organExaminationDTO1).isNotEqualTo(organExaminationDTO2);
        organExaminationDTO2.setId(organExaminationDTO1.getId());
        assertThat(organExaminationDTO1).isEqualTo(organExaminationDTO2);
        organExaminationDTO2.setId(2L);
        assertThat(organExaminationDTO1).isNotEqualTo(organExaminationDTO2);
        organExaminationDTO1.setId(null);
        assertThat(organExaminationDTO1).isNotEqualTo(organExaminationDTO2);
    }
}
