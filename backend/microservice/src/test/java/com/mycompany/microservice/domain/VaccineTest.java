package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.VaccinationForBabyTestSamples.*;
import static com.mycompany.microservice.domain.VaccineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vaccine.class);
        Vaccine vaccine1 = getVaccineSample1();
        Vaccine vaccine2 = new Vaccine();
        assertThat(vaccine1).isNotEqualTo(vaccine2);

        vaccine2.setId(vaccine1.getId());
        assertThat(vaccine1).isEqualTo(vaccine2);

        vaccine2 = getVaccineSample2();
        assertThat(vaccine1).isNotEqualTo(vaccine2);
    }

    @Test
    void vaccinationForBabyTest() {
        Vaccine vaccine = getVaccineRandomSampleGenerator();
        VaccinationForBaby vaccinationForBabyBack = getVaccinationForBabyRandomSampleGenerator();

        vaccine.setVaccinationForBaby(vaccinationForBabyBack);
        assertThat(vaccine.getVaccinationForBaby()).isEqualTo(vaccinationForBabyBack);

        vaccine.vaccinationForBaby(null);
        assertThat(vaccine.getVaccinationForBaby()).isNull();
    }
}
