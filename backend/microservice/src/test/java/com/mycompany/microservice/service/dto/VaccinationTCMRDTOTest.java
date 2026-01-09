package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationTCMRDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationTCMRDTO.class);
        VaccinationTCMRDTO vaccinationTCMRDTO1 = new VaccinationTCMRDTO();
        vaccinationTCMRDTO1.setId(1L);
        VaccinationTCMRDTO vaccinationTCMRDTO2 = new VaccinationTCMRDTO();
        assertThat(vaccinationTCMRDTO1).isNotEqualTo(vaccinationTCMRDTO2);
        vaccinationTCMRDTO2.setId(vaccinationTCMRDTO1.getId());
        assertThat(vaccinationTCMRDTO1).isEqualTo(vaccinationTCMRDTO2);
        vaccinationTCMRDTO2.setId(2L);
        assertThat(vaccinationTCMRDTO1).isNotEqualTo(vaccinationTCMRDTO2);
        vaccinationTCMRDTO1.setId(null);
        assertThat(vaccinationTCMRDTO1).isNotEqualTo(vaccinationTCMRDTO2);
    }
}
