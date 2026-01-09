package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurgeryHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurgeryHistoryDTO.class);
        SurgeryHistoryDTO surgeryHistoryDTO1 = new SurgeryHistoryDTO();
        surgeryHistoryDTO1.setId(1L);
        SurgeryHistoryDTO surgeryHistoryDTO2 = new SurgeryHistoryDTO();
        assertThat(surgeryHistoryDTO1).isNotEqualTo(surgeryHistoryDTO2);
        surgeryHistoryDTO2.setId(surgeryHistoryDTO1.getId());
        assertThat(surgeryHistoryDTO1).isEqualTo(surgeryHistoryDTO2);
        surgeryHistoryDTO2.setId(2L);
        assertThat(surgeryHistoryDTO1).isNotEqualTo(surgeryHistoryDTO2);
        surgeryHistoryDTO1.setId(null);
        assertThat(surgeryHistoryDTO1).isNotEqualTo(surgeryHistoryDTO2);
    }
}
