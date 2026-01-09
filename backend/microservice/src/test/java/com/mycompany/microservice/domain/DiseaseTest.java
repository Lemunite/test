package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.DiseaseTestSamples.*;
import static com.mycompany.microservice.domain.FamilyDiseaseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiseaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disease.class);
        Disease disease1 = getDiseaseSample1();
        Disease disease2 = new Disease();
        assertThat(disease1).isNotEqualTo(disease2);

        disease2.setId(disease1.getId());
        assertThat(disease1).isEqualTo(disease2);

        disease2 = getDiseaseSample2();
        assertThat(disease1).isNotEqualTo(disease2);
    }

    @Test
    void familyDiseaseTest() {
        Disease disease = getDiseaseRandomSampleGenerator();
        FamilyDisease familyDiseaseBack = getFamilyDiseaseRandomSampleGenerator();

        disease.setFamilyDisease(familyDiseaseBack);
        assertThat(disease.getFamilyDisease()).isEqualTo(familyDiseaseBack);

        disease.familyDisease(null);
        assertThat(disease.getFamilyDisease()).isNull();
    }
}
