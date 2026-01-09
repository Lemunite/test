package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static com.mycompany.microservice.domain.SurgeryHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurgeryHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurgeryHistory.class);
        SurgeryHistory surgeryHistory1 = getSurgeryHistorySample1();
        SurgeryHistory surgeryHistory2 = new SurgeryHistory();
        assertThat(surgeryHistory1).isNotEqualTo(surgeryHistory2);

        surgeryHistory2.setId(surgeryHistory1.getId());
        assertThat(surgeryHistory1).isEqualTo(surgeryHistory2);

        surgeryHistory2 = getSurgeryHistorySample2();
        assertThat(surgeryHistory1).isNotEqualTo(surgeryHistory2);
    }

    @Test
    void patientTest() {
        SurgeryHistory surgeryHistory = getSurgeryHistoryRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        surgeryHistory.setPatient(patientBack);
        assertThat(surgeryHistory.getPatient()).isEqualTo(patientBack);

        surgeryHistory.patient(null);
        assertThat(surgeryHistory.getPatient()).isNull();
    }
}
