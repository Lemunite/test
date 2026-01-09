package com.mycompany.microservice.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.MedicalRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate examinationDate;

    @Lob
    private String medicalHistory;

    private Double leftEyeNoGlass;

    private Double rightEyeNoGlass;

    private Double leftEyeWithGlass;

    private Double rightEyeWithGlass;

    private Integer pulse;

    private Double temperature;

    private String bloodPressure;

    private Integer respiratoryRate;

    private Double weight;

    private Double height;

    private Double bmi;

    private Double waist;

    private String skinMucosa;

    private String other;

    @NotNull
    private String diseaseName;

    private String diseaseCode;

    @Lob
    private String advice;

    @NotNull
    private String docterName;

    private OrganExaminationDTO organExamination;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(LocalDate examinationDate) {
        this.examinationDate = examinationDate;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Double getLeftEyeNoGlass() {
        return leftEyeNoGlass;
    }

    public void setLeftEyeNoGlass(Double leftEyeNoGlass) {
        this.leftEyeNoGlass = leftEyeNoGlass;
    }

    public Double getRightEyeNoGlass() {
        return rightEyeNoGlass;
    }

    public void setRightEyeNoGlass(Double rightEyeNoGlass) {
        this.rightEyeNoGlass = rightEyeNoGlass;
    }

    public Double getLeftEyeWithGlass() {
        return leftEyeWithGlass;
    }

    public void setLeftEyeWithGlass(Double leftEyeWithGlass) {
        this.leftEyeWithGlass = leftEyeWithGlass;
    }

    public Double getRightEyeWithGlass() {
        return rightEyeWithGlass;
    }

    public void setRightEyeWithGlass(Double rightEyeWithGlass) {
        this.rightEyeWithGlass = rightEyeWithGlass;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Double getWaist() {
        return waist;
    }

    public void setWaist(Double waist) {
        this.waist = waist;
    }

    public String getSkinMucosa() {
        return skinMucosa;
    }

    public void setSkinMucosa(String skinMucosa) {
        this.skinMucosa = skinMucosa;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getDocterName() {
        return docterName;
    }

    public void setDocterName(String docterName) {
        this.docterName = docterName;
    }

    public OrganExaminationDTO getOrganExamination() {
        return organExamination;
    }

    public void setOrganExamination(OrganExaminationDTO organExamination) {
        this.organExamination = organExamination;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalRecordDTO)) {
            return false;
        }

        MedicalRecordDTO medicalRecordDTO = (MedicalRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecordDTO{" +
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
            ", organExamination=" + getOrganExamination() +
            ", patient=" + getPatient() +
            "}";
    }
}
