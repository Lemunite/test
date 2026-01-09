package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyDiseaseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyDiseaseDTO.class);
        FamilyDiseaseDTO familyDiseaseDTO1 = new FamilyDiseaseDTO();
        familyDiseaseDTO1.setId(1L);
        FamilyDiseaseDTO familyDiseaseDTO2 = new FamilyDiseaseDTO();
        assertThat(familyDiseaseDTO1).isNotEqualTo(familyDiseaseDTO2);
        familyDiseaseDTO2.setId(familyDiseaseDTO1.getId());
        assertThat(familyDiseaseDTO1).isEqualTo(familyDiseaseDTO2);
        familyDiseaseDTO2.setId(2L);
        assertThat(familyDiseaseDTO1).isNotEqualTo(familyDiseaseDTO2);
        familyDiseaseDTO1.setId(null);
        assertThat(familyDiseaseDTO1).isNotEqualTo(familyDiseaseDTO2);
    }
}
