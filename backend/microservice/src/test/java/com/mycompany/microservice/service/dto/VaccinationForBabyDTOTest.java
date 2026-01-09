package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationForBabyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationForBabyDTO.class);
        VaccinationForBabyDTO vaccinationForBabyDTO1 = new VaccinationForBabyDTO();
        vaccinationForBabyDTO1.setId(1L);
        VaccinationForBabyDTO vaccinationForBabyDTO2 = new VaccinationForBabyDTO();
        assertThat(vaccinationForBabyDTO1).isNotEqualTo(vaccinationForBabyDTO2);
        vaccinationForBabyDTO2.setId(vaccinationForBabyDTO1.getId());
        assertThat(vaccinationForBabyDTO1).isEqualTo(vaccinationForBabyDTO2);
        vaccinationForBabyDTO2.setId(2L);
        assertThat(vaccinationForBabyDTO1).isNotEqualTo(vaccinationForBabyDTO2);
        vaccinationForBabyDTO1.setId(null);
        assertThat(vaccinationForBabyDTO1).isNotEqualTo(vaccinationForBabyDTO2);
    }
}
