package com.mycompany.microservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.OrganExamination} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganExaminationDTO implements Serializable {

    private Long id;

    private String cardiovascular;

    private String respiratory;

    private String digestive;

    private String urinary;

    private String musculoskeletal;

    private String endocrine;

    private String neurological;

    private String psychiatric;

    private String surgery;

    private String obstetricsAndGynecology;

    private String otolaryngology;

    private String dentistryAndMaxillofacialSurgery;

    private String eye;

    private String dermatology;

    private String nutrition;

    private String exercise;

    private String other;

    private String developmentAssessment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardiovascular() {
        return cardiovascular;
    }

    public void setCardiovascular(String cardiovascular) {
        this.cardiovascular = cardiovascular;
    }

    public String getRespiratory() {
        return respiratory;
    }

    public void setRespiratory(String respiratory) {
        this.respiratory = respiratory;
    }

    public String getDigestive() {
        return digestive;
    }

    public void setDigestive(String digestive) {
        this.digestive = digestive;
    }

    public String getUrinary() {
        return urinary;
    }

    public void setUrinary(String urinary) {
        this.urinary = urinary;
    }

    public String getMusculoskeletal() {
        return musculoskeletal;
    }

    public void setMusculoskeletal(String musculoskeletal) {
        this.musculoskeletal = musculoskeletal;
    }

    public String getEndocrine() {
        return endocrine;
    }

    public void setEndocrine(String endocrine) {
        this.endocrine = endocrine;
    }

    public String getNeurological() {
        return neurological;
    }

    public void setNeurological(String neurological) {
        this.neurological = neurological;
    }

    public String getPsychiatric() {
        return psychiatric;
    }

    public void setPsychiatric(String psychiatric) {
        this.psychiatric = psychiatric;
    }

    public String getSurgery() {
        return surgery;
    }

    public void setSurgery(String surgery) {
        this.surgery = surgery;
    }

    public String getObstetricsAndGynecology() {
        return obstetricsAndGynecology;
    }

    public void setObstetricsAndGynecology(String obstetricsAndGynecology) {
        this.obstetricsAndGynecology = obstetricsAndGynecology;
    }

    public String getOtolaryngology() {
        return otolaryngology;
    }

    public void setOtolaryngology(String otolaryngology) {
        this.otolaryngology = otolaryngology;
    }

    public String getDentistryAndMaxillofacialSurgery() {
        return dentistryAndMaxillofacialSurgery;
    }

    public void setDentistryAndMaxillofacialSurgery(String dentistryAndMaxillofacialSurgery) {
        this.dentistryAndMaxillofacialSurgery = dentistryAndMaxillofacialSurgery;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getDermatology() {
        return dermatology;
    }

    public void setDermatology(String dermatology) {
        this.dermatology = dermatology;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDevelopmentAssessment() {
        return developmentAssessment;
    }

    public void setDevelopmentAssessment(String developmentAssessment) {
        this.developmentAssessment = developmentAssessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganExaminationDTO)) {
            return false;
        }

        OrganExaminationDTO organExaminationDTO = (OrganExaminationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, organExaminationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganExaminationDTO{" +
            "id=" + getId() +
            ", cardiovascular='" + getCardiovascular() + "'" +
            ", respiratory='" + getRespiratory() + "'" +
            ", digestive='" + getDigestive() + "'" +
            ", urinary='" + getUrinary() + "'" +
            ", musculoskeletal='" + getMusculoskeletal() + "'" +
            ", endocrine='" + getEndocrine() + "'" +
            ", neurological='" + getNeurological() + "'" +
            ", psychiatric='" + getPsychiatric() + "'" +
            ", surgery='" + getSurgery() + "'" +
            ", obstetricsAndGynecology='" + getObstetricsAndGynecology() + "'" +
            ", otolaryngology='" + getOtolaryngology() + "'" +
            ", dentistryAndMaxillofacialSurgery='" + getDentistryAndMaxillofacialSurgery() + "'" +
            ", eye='" + getEye() + "'" +
            ", dermatology='" + getDermatology() + "'" +
            ", nutrition='" + getNutrition() + "'" +
            ", exercise='" + getExercise() + "'" +
            ", other='" + getOther() + "'" +
            ", developmentAssessment='" + getDevelopmentAssessment() + "'" +
            "}";
    }
}
