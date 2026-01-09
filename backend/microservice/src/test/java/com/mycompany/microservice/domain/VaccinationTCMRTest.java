package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static com.mycompany.microservice.domain.VaccinationTCMRTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationTCMRTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationTCMR.class);
        VaccinationTCMR vaccinationTCMR1 = getVaccinationTCMRSample1();
        VaccinationTCMR vaccinationTCMR2 = new VaccinationTCMR();
        assertThat(vaccinationTCMR1).isNotEqualTo(vaccinationTCMR2);

        vaccinationTCMR2.setId(vaccinationTCMR1.getId());
        assertThat(vaccinationTCMR1).isEqualTo(vaccinationTCMR2);

        vaccinationTCMR2 = getVaccinationTCMRSample2();
        assertThat(vaccinationTCMR1).isNotEqualTo(vaccinationTCMR2);
    }

    @Test
    void patientTest() {
        VaccinationTCMR vaccinationTCMR = getVaccinationTCMRRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        vaccinationTCMR.setPatient(patientBack);
        assertThat(vaccinationTCMR.getPatient()).isEqualTo(patientBack);

        vaccinationTCMR.patient(null);
        assertThat(vaccinationTCMR.getPatient()).isNull();
    }
}
