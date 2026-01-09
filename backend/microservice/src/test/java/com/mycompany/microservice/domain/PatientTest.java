package com.mycompany.microservice.domain;

import static com.mycompany.microservice.domain.AdditionalInformationTestSamples.*;
import static com.mycompany.microservice.domain.AllergyTestSamples.*;
import static com.mycompany.microservice.domain.DisabilityTestSamples.*;
import static com.mycompany.microservice.domain.FamilyAllergyTestSamples.*;
import static com.mycompany.microservice.domain.FamilyDiseaseTestSamples.*;
import static com.mycompany.microservice.domain.MedicalRecordTestSamples.*;
import static com.mycompany.microservice.domain.PatientTestSamples.*;
import static com.mycompany.microservice.domain.PregnancyTetanusTestSamples.*;
import static com.mycompany.microservice.domain.SurgeryHistoryTestSamples.*;
import static com.mycompany.microservice.domain.VaccinationForBabyTestSamples.*;
import static com.mycompany.microservice.domain.VaccinationTCMRTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.microservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }

    @Test
    void additionalInfoTest() {
        Patient patient = getPatientRandomSampleGenerator();
        AdditionalInformation additionalInformationBack = getAdditionalInformationRandomSampleGenerator();

        patient.setAdditionalInfo(additionalInformationBack);
        assertThat(patient.getAdditionalInfo()).isEqualTo(additionalInformationBack);

        patient.additionalInfo(null);
        assertThat(patient.getAdditionalInfo()).isNull();
    }

    @Test
    void vaccinationsForBabyTest() {
        Patient patient = getPatientRandomSampleGenerator();
        VaccinationForBaby vaccinationForBabyBack = getVaccinationForBabyRandomSampleGenerator();

        patient.setVaccinationsForBaby(vaccinationForBabyBack);
        assertThat(patient.getVaccinationsForBaby()).isEqualTo(vaccinationForBabyBack);

        patient.vaccinationsForBaby(null);
        assertThat(patient.getVaccinationsForBaby()).isNull();
    }

    @Test
    void allergiesTest() {
        Patient patient = getPatientRandomSampleGenerator();
        Allergy allergyBack = getAllergyRandomSampleGenerator();

        patient.addAllergies(allergyBack);
        assertThat(patient.getAllergies()).containsOnly(allergyBack);
        assertThat(allergyBack.getPatient()).isEqualTo(patient);

        patient.removeAllergies(allergyBack);
        assertThat(patient.getAllergies()).doesNotContain(allergyBack);
        assertThat(allergyBack.getPatient()).isNull();

        patient.allergies(new HashSet<>(Set.of(allergyBack)));
        assertThat(patient.getAllergies()).containsOnly(allergyBack);
        assertThat(allergyBack.getPatient()).isEqualTo(patient);

        patient.setAllergies(new HashSet<>());
        assertThat(patient.getAllergies()).doesNotContain(allergyBack);
        assertThat(allergyBack.getPatient()).isNull();
    }

    @Test
    void disabilitiesTest() {
        Patient patient = getPatientRandomSampleGenerator();
        Disability disabilityBack = getDisabilityRandomSampleGenerator();

        patient.addDisabilities(disabilityBack);
        assertThat(patient.getDisabilities()).containsOnly(disabilityBack);
        assertThat(disabilityBack.getPatient()).isEqualTo(patient);

        patient.removeDisabilities(disabilityBack);
        assertThat(patient.getDisabilities()).doesNotContain(disabilityBack);
        assertThat(disabilityBack.getPatient()).isNull();

        patient.disabilities(new HashSet<>(Set.of(disabilityBack)));
        assertThat(patient.getDisabilities()).containsOnly(disabilityBack);
        assertThat(disabilityBack.getPatient()).isEqualTo(patient);

        patient.setDisabilities(new HashSet<>());
        assertThat(patient.getDisabilities()).doesNotContain(disabilityBack);
        assertThat(disabilityBack.getPatient()).isNull();
    }

    @Test
    void surgeryHistoriesTest() {
        Patient patient = getPatientRandomSampleGenerator();
        SurgeryHistory surgeryHistoryBack = getSurgeryHistoryRandomSampleGenerator();

        patient.addSurgeryHistories(surgeryHistoryBack);
        assertThat(patient.getSurgeryHistories()).containsOnly(surgeryHistoryBack);
        assertThat(surgeryHistoryBack.getPatient()).isEqualTo(patient);

        patient.removeSurgeryHistories(surgeryHistoryBack);
        assertThat(patient.getSurgeryHistories()).doesNotContain(surgeryHistoryBack);
        assertThat(surgeryHistoryBack.getPatient()).isNull();

        patient.surgeryHistories(new HashSet<>(Set.of(surgeryHistoryBack)));
        assertThat(patient.getSurgeryHistories()).containsOnly(surgeryHistoryBack);
        assertThat(surgeryHistoryBack.getPatient()).isEqualTo(patient);

        patient.setSurgeryHistories(new HashSet<>());
        assertThat(patient.getSurgeryHistories()).doesNotContain(surgeryHistoryBack);
        assertThat(surgeryHistoryBack.getPatient()).isNull();
    }

    @Test
    void familyAllergiesTest() {
        Patient patient = getPatientRandomSampleGenerator();
        FamilyAllergy familyAllergyBack = getFamilyAllergyRandomSampleGenerator();

        patient.addFamilyAllergies(familyAllergyBack);
        assertThat(patient.getFamilyAllergies()).containsOnly(familyAllergyBack);
        assertThat(familyAllergyBack.getPatient()).isEqualTo(patient);

        patient.removeFamilyAllergies(familyAllergyBack);
        assertThat(patient.getFamilyAllergies()).doesNotContain(familyAllergyBack);
        assertThat(familyAllergyBack.getPatient()).isNull();

        patient.familyAllergies(new HashSet<>(Set.of(familyAllergyBack)));
        assertThat(patient.getFamilyAllergies()).containsOnly(familyAllergyBack);
        assertThat(familyAllergyBack.getPatient()).isEqualTo(patient);

        patient.setFamilyAllergies(new HashSet<>());
        assertThat(patient.getFamilyAllergies()).doesNotContain(familyAllergyBack);
        assertThat(familyAllergyBack.getPatient()).isNull();
    }

    @Test
    void familyDiseasesTest() {
        Patient patient = getPatientRandomSampleGenerator();
        FamilyDisease familyDiseaseBack = getFamilyDiseaseRandomSampleGenerator();

        patient.addFamilyDiseases(familyDiseaseBack);
        assertThat(patient.getFamilyDiseases()).containsOnly(familyDiseaseBack);
        assertThat(familyDiseaseBack.getPatient()).isEqualTo(patient);

        patient.removeFamilyDiseases(familyDiseaseBack);
        assertThat(patient.getFamilyDiseases()).doesNotContain(familyDiseaseBack);
        assertThat(familyDiseaseBack.getPatient()).isNull();

        patient.familyDiseases(new HashSet<>(Set.of(familyDiseaseBack)));
        assertThat(patient.getFamilyDiseases()).containsOnly(familyDiseaseBack);
        assertThat(familyDiseaseBack.getPatient()).isEqualTo(patient);

        patient.setFamilyDiseases(new HashSet<>());
        assertThat(patient.getFamilyDiseases()).doesNotContain(familyDiseaseBack);
        assertThat(familyDiseaseBack.getPatient()).isNull();
    }

    @Test
    void vaccinationsTCMRTest() {
        Patient patient = getPatientRandomSampleGenerator();
        VaccinationTCMR vaccinationTCMRBack = getVaccinationTCMRRandomSampleGenerator();

        patient.addVaccinationsTCMR(vaccinationTCMRBack);
        assertThat(patient.getVaccinationsTCMRS()).containsOnly(vaccinationTCMRBack);
        assertThat(vaccinationTCMRBack.getPatient()).isEqualTo(patient);

        patient.removeVaccinationsTCMR(vaccinationTCMRBack);
        assertThat(patient.getVaccinationsTCMRS()).doesNotContain(vaccinationTCMRBack);
        assertThat(vaccinationTCMRBack.getPatient()).isNull();

        patient.vaccinationsTCMRS(new HashSet<>(Set.of(vaccinationTCMRBack)));
        assertThat(patient.getVaccinationsTCMRS()).containsOnly(vaccinationTCMRBack);
        assertThat(vaccinationTCMRBack.getPatient()).isEqualTo(patient);

        patient.setVaccinationsTCMRS(new HashSet<>());
        assertThat(patient.getVaccinationsTCMRS()).doesNotContain(vaccinationTCMRBack);
        assertThat(vaccinationTCMRBack.getPatient()).isNull();
    }

    @Test
    void pregnancyTetanusTest() {
        Patient patient = getPatientRandomSampleGenerator();
        PregnancyTetanus pregnancyTetanusBack = getPregnancyTetanusRandomSampleGenerator();

        patient.addPregnancyTetanus(pregnancyTetanusBack);
        assertThat(patient.getPregnancyTetanuses()).containsOnly(pregnancyTetanusBack);
        assertThat(pregnancyTetanusBack.getPatient()).isEqualTo(patient);

        patient.removePregnancyTetanus(pregnancyTetanusBack);
        assertThat(patient.getPregnancyTetanuses()).doesNotContain(pregnancyTetanusBack);
        assertThat(pregnancyTetanusBack.getPatient()).isNull();

        patient.pregnancyTetanuses(new HashSet<>(Set.of(pregnancyTetanusBack)));
        assertThat(patient.getPregnancyTetanuses()).containsOnly(pregnancyTetanusBack);
        assertThat(pregnancyTetanusBack.getPatient()).isEqualTo(patient);

        patient.setPregnancyTetanuses(new HashSet<>());
        assertThat(patient.getPregnancyTetanuses()).doesNotContain(pregnancyTetanusBack);
        assertThat(pregnancyTetanusBack.getPatient()).isNull();
    }

    @Test
    void medicalRecordsTest() {
        Patient patient = getPatientRandomSampleGenerator();
        MedicalRecord medicalRecordBack = getMedicalRecordRandomSampleGenerator();

        patient.addMedicalRecords(medicalRecordBack);
        assertThat(patient.getMedicalRecords()).containsOnly(medicalRecordBack);
        assertThat(medicalRecordBack.getPatient()).isEqualTo(patient);

        patient.removeMedicalRecords(medicalRecordBack);
        assertThat(patient.getMedicalRecords()).doesNotContain(medicalRecordBack);
        assertThat(medicalRecordBack.getPatient()).isNull();

        patient.medicalRecords(new HashSet<>(Set.of(medicalRecordBack)));
        assertThat(patient.getMedicalRecords()).containsOnly(medicalRecordBack);
        assertThat(medicalRecordBack.getPatient()).isEqualTo(patient);

        patient.setMedicalRecords(new HashSet<>());
        assertThat(patient.getMedicalRecords()).doesNotContain(medicalRecordBack);
        assertThat(medicalRecordBack.getPatient()).isNull();
    }
}
