package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A MedicalRecord.
 */
@Entity
@Table(name = "medical_record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "examination_date", nullable = false)
    private LocalDate examinationDate;

    @Column(name = "medical_history")
    private String medicalHistory;

    @Column(name = "left_eye_no_glass")
    private Double leftEyeNoGlass;

    @Column(name = "right_eye_no_glass")
    private Double rightEyeNoGlass;

    @Column(name = "left_eye_with_glass")
    private Double leftEyeWithGlass;

    @Column(name = "right_eye_with_glass")
    private Double rightEyeWithGlass;

    @Column(name = "pulse")
    private Integer pulse;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "waist")
    private Double waist;

    @Column(name = "skin_mucosa")
    private String skinMucosa;

    @Column(name = "other")
    private String other;

    @NotNull
    @Column(name = "disease_name", nullable = false)
    private String diseaseName;

    @Column(name = "disease_code")
    private String diseaseCode;

    @Column(name = "advice")
    private String advice;

    @NotNull
    @Column(name = "docter_name", nullable = false)
    private String docterName;

    @JsonIgnoreProperties(value = { "medicalRecord" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private OrganExamination organExamination;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "medicalRecord")
    @JsonIgnoreProperties(value = { "medicalRecord" }, allowSetters = true)
    private Set<ParaclinicalResult> paraclinicalResults = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "additionalInfo",
            "vaccinationsForBaby",
            "allergies",
            "disabilities",
            "surgeryHistories",
            "familyAllergies",
            "familyDiseases",
            "vaccinationsTCMRS",
            "pregnancyTetanuses",
            "medicalRecords",
        },
        allowSetters = true
    )
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExaminationDate() {
        return this.examinationDate;
    }

    public MedicalRecord examinationDate(LocalDate examinationDate) {
        this.setExaminationDate(examinationDate);
        return this;
    }

    public void setExaminationDate(LocalDate examinationDate) {
        this.examinationDate = examinationDate;
    }

    public String getMedicalHistory() {
        return this.medicalHistory;
    }

    public MedicalRecord medicalHistory(String medicalHistory) {
        this.setMedicalHistory(medicalHistory);
        return this;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Double getLeftEyeNoGlass() {
        return this.leftEyeNoGlass;
    }

    public MedicalRecord leftEyeNoGlass(Double leftEyeNoGlass) {
        this.setLeftEyeNoGlass(leftEyeNoGlass);
        return this;
    }

    public void setLeftEyeNoGlass(Double leftEyeNoGlass) {
        this.leftEyeNoGlass = leftEyeNoGlass;
    }

    public Double getRightEyeNoGlass() {
        return this.rightEyeNoGlass;
    }

    public MedicalRecord rightEyeNoGlass(Double rightEyeNoGlass) {
        this.setRightEyeNoGlass(rightEyeNoGlass);
        return this;
    }

    public void setRightEyeNoGlass(Double rightEyeNoGlass) {
        this.rightEyeNoGlass = rightEyeNoGlass;
    }

    public Double getLeftEyeWithGlass() {
        return this.leftEyeWithGlass;
    }

    public MedicalRecord leftEyeWithGlass(Double leftEyeWithGlass) {
        this.setLeftEyeWithGlass(leftEyeWithGlass);
        return this;
    }

    public void setLeftEyeWithGlass(Double leftEyeWithGlass) {
        this.leftEyeWithGlass = leftEyeWithGlass;
    }

    public Double getRightEyeWithGlass() {
        return this.rightEyeWithGlass;
    }

    public MedicalRecord rightEyeWithGlass(Double rightEyeWithGlass) {
        this.setRightEyeWithGlass(rightEyeWithGlass);
        return this;
    }

    public void setRightEyeWithGlass(Double rightEyeWithGlass) {
        this.rightEyeWithGlass = rightEyeWithGlass;
    }

    public Integer getPulse() {
        return this.pulse;
    }

    public MedicalRecord pulse(Integer pulse) {
        this.setPulse(pulse);
        return this;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public MedicalRecord temperature(Double temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return this.bloodPressure;
    }

    public MedicalRecord bloodPressure(String bloodPressure) {
        this.setBloodPressure(bloodPressure);
        return this;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getRespiratoryRate() {
        return this.respiratoryRate;
    }

    public MedicalRecord respiratoryRate(Integer respiratoryRate) {
        this.setRespiratoryRate(respiratoryRate);
        return this;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Double getWeight() {
        return this.weight;
    }

    public MedicalRecord weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return this.height;
    }

    public MedicalRecord height(Double height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getBmi() {
        return this.bmi;
    }

    public MedicalRecord bmi(Double bmi) {
        this.setBmi(bmi);
        return this;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Double getWaist() {
        return this.waist;
    }

    public MedicalRecord waist(Double waist) {
        this.setWaist(waist);
        return this;
    }

    public void setWaist(Double waist) {
        this.waist = waist;
    }

    public String getSkinMucosa() {
        return this.skinMucosa;
    }

    public MedicalRecord skinMucosa(String skinMucosa) {
        this.setSkinMucosa(skinMucosa);
        return this;
    }

    public void setSkinMucosa(String skinMucosa) {
        this.skinMucosa = skinMucosa;
    }

    public String getOther() {
        return this.other;
    }

    public MedicalRecord other(String other) {
        this.setOther(other);
        return this;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDiseaseName() {
        return this.diseaseName;
    }

    public MedicalRecord diseaseName(String diseaseName) {
        this.setDiseaseName(diseaseName);
        return this;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseCode() {
        return this.diseaseCode;
    }

    public MedicalRecord diseaseCode(String diseaseCode) {
        this.setDiseaseCode(diseaseCode);
        return this;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getAdvice() {
        return this.advice;
    }

    public MedicalRecord advice(String advice) {
        this.setAdvice(advice);
        return this;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getDocterName() {
        return this.docterName;
    }

    public MedicalRecord docterName(String docterName) {
        this.setDocterName(docterName);
        return this;
    }

    public void setDocterName(String docterName) {
        this.docterName = docterName;
    }

    public OrganExamination getOrganExamination() {
        return this.organExamination;
    }

    public void setOrganExamination(OrganExamination organExamination) {
        this.organExamination = organExamination;
    }

    public MedicalRecord organExamination(OrganExamination organExamination) {
        this.setOrganExamination(organExamination);
        return this;
    }

    public Set<ParaclinicalResult> getParaclinicalResults() {
        return this.paraclinicalResults;
    }

    public void setParaclinicalResults(Set<ParaclinicalResult> paraclinicalResults) {
        if (this.paraclinicalResults != null) {
            this.paraclinicalResults.forEach(i -> i.setMedicalRecord(null));
        }
        if (paraclinicalResults != null) {
            paraclinicalResults.forEach(i -> i.setMedicalRecord(this));
        }
        this.paraclinicalResults = paraclinicalResults;
    }

    public MedicalRecord paraclinicalResults(Set<ParaclinicalResult> paraclinicalResults) {
        this.setParaclinicalResults(paraclinicalResults);
        return this;
    }

    public MedicalRecord addParaclinicalResults(ParaclinicalResult paraclinicalResult) {
        this.paraclinicalResults.add(paraclinicalResult);
        paraclinicalResult.setMedicalRecord(this);
        return this;
    }

    public MedicalRecord removeParaclinicalResults(ParaclinicalResult paraclinicalResult) {
        this.paraclinicalResults.remove(paraclinicalResult);
        paraclinicalResult.setMedicalRecord(null);
        return this;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MedicalRecord patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecord{" +
            "id=" + getId() +
            ", examinationDate='" + getExaminationDate() + "'" +
            ", medicalHistory='" + getMedicalHistory() + "'" +
            ", leftEyeNoGlass=" + getLeftEyeNoGlass() +
            ", rightEyeNoGlass=" + getRightEyeNoGlass() +
            ", leftEyeWithGlass=" + getLeftEyeWithGlass() +
            ", rightEyeWithGlass=" + getRightEyeWithGlass() +
            ", pulse=" + getPulse() +
            ", temperature=" + getTemperature() +
            ", bloodPressure='" + getBloodPressure() + "'" +
            ", respiratoryRate=" + getRespiratoryRate() +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", bmi=" + getBmi() +
            ", waist=" + getWaist() +
            ", skinMucosa='" + getSkinMucosa() + "'" +
            ", other='" + getOther() + "'" +
            ", diseaseName='" + getDiseaseName() + "'" +
            ", diseaseCode='" + getDiseaseCode() + "'" +
            ", advice='" + getAdvice() + "'" +
            ", docterName='" + getDocterName() + "'" +
            "}";
    }
}
