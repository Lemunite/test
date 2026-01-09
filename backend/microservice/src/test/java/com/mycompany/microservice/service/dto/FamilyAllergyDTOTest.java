package com.mycompany.microservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyAllergyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyAllergyDTO.class);
        FamilyAllergyDTO familyAllergyDTO1 = new FamilyAllergyDTO();
        familyAllergyDTO1.setId(1L);
        FamilyAllergyDTO familyAllergyDTO2 = new FamilyAllergyDTO();
        assertThat(familyAllergyDTO1).isNotEqualTo(familyAllergyDTO2);
        familyAllergyDTO2.setId(familyAllergyDTO1.getId());
        assertThat(familyAllergyDTO1).isEqualTo(familyAllergyDTO2);
        familyAllergyDTO2.setId(2L);
        assertThat(familyAllergyDTO1).isNotEqualTo(familyAllergyDTO2);
        familyAllergyDTO1.setId(null);
        assertThat(familyAllergyDTO1).isNotEqualTo(familyAllergyDTO2);
    }
}
