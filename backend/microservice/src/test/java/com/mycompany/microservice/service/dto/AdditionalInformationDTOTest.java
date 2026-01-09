package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdditionalInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdditionalInformationDTO.class);
        AdditionalInformationDTO additionalInformationDTO1 = new AdditionalInformationDTO();
        additionalInformationDTO1.setId(1L);
        AdditionalInformationDTO additionalInformationDTO2 = new AdditionalInformationDTO();
        assertThat(additionalInformationDTO1).isNotEqualTo(additionalInformationDTO2);
        additionalInformationDTO2.setId(additionalInformationDTO1.getId());
        assertThat(additionalInformationDTO1).isEqualTo(additionalInformationDTO2);
        additionalInformationDTO2.setId(2L);
        assertThat(additionalInformationDTO1).isNotEqualTo(additionalInformationDTO2);
        additionalInformationDTO1.setId(null);
        assertThat(additionalInformationDTO1).isNotEqualTo(additionalInformationDTO2);
    }
}
