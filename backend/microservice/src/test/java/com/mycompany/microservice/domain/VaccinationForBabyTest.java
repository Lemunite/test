package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static com.mycompany.microservice.domain.VaccinationForBabyTestSamples.*;
import static com.mycompany.microservice.domain.VaccineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VaccinationForBabyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationForBaby.class);
        VaccinationForBaby vaccinationForBaby1 = getVaccinationForBabySample1();
        VaccinationForBaby vaccinationForBaby2 = new VaccinationForBaby();
        assertThat(vaccinationForBaby1).isNotEqualTo(vaccinationForBaby2);

        vaccinationForBaby2.setId(vaccinationForBaby1.getId());
        assertThat(vaccinationForBaby1).isEqualTo(vaccinationForBaby2);

        vaccinationForBaby2 = getVaccinationForBabySample2();
        assertThat(vaccinationForBaby1).isNotEqualTo(vaccinationForBaby2);
    }

    @Test
    void vaccineTest() {
        VaccinationForBaby vaccinationForBaby = getVaccinationForBabyRandomSampleGenerator();
        Vaccine vaccineBack = getVaccineRandomSampleGenerator();

        vaccinationForBaby.addVaccine(vaccineBack);
        assertThat(vaccinationForBaby.getVaccines()).containsOnly(vaccineBack);
        assertThat(vaccineBack.getVaccinationForBaby()).isEqualTo(vaccinationForBaby);

        vaccinationForBaby.removeVaccine(vaccineBack);
        assertThat(vaccinationForBaby.getVaccines()).doesNotContain(vaccineBack);
        assertThat(vaccineBack.getVaccinationForBaby()).isNull();

        vaccinationForBaby.vaccines(new HashSet<>(Set.of(vaccineBack)));
        assertThat(vaccinationForBaby.getVaccines()).containsOnly(vaccineBack);
        assertThat(vaccineBack.getVaccinationForBaby()).isEqualTo(vaccinationForBaby);

        vaccinationForBaby.setVaccines(new HashSet<>());
        assertThat(vaccinationForBaby.getVaccines()).doesNotContain(vaccineBack);
        assertThat(vaccineBack.getVaccinationForBaby()).isNull();
    }

    @Test
    void patientTest() {
        VaccinationForBaby vaccinationForBaby = getVaccinationForBabyRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        vaccinationForBaby.setPatient(patientBack);
        assertThat(vaccinationForBaby.getPatient()).isEqualTo(patientBack);
        assertThat(patientBack.getVaccinationsForBaby()).isEqualTo(vaccinationForBaby);

        vaccinationForBaby.patient(null);
        assertThat(vaccinationForBaby.getPatient()).isNull();
        assertThat(patientBack.getVaccinationsForBaby()).isNull();
    }
}
