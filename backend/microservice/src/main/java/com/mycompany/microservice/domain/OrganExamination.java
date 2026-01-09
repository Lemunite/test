package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A OrganExamination.
 */
@Entity
@Table(name = "organ_examination")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganExamination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cardiovascular")
    private String cardiovascular;

    @Column(name = "respiratory")
    private String respiratory;

    @Column(name = "digestive")
    private String digestive;

    @Column(name = "urinary")
    private String urinary;

    @Column(name = "musculoskeletal")
    private String musculoskeletal;

    @Column(name = "endocrine")
    private String endocrine;

    @Column(name = "neurological")
    private String neurological;

    @Column(name = "psychiatric")
    private String psychiatric;

    @Column(name = "surgery")
    private String surgery;

    @Column(name = "obstetrics_and_gynecology")
    private String obstetricsAndGynecology;

    @Column(name = "otolaryngology")
    private String otolaryngology;

    @Column(name = "dentistry_and_maxillofacial_surgery")
    private String dentistryAndMaxillofacialSurgery;

    @Column(name = "eye")
    private String eye;

    @Column(name = "dermatology")
    private String dermatology;

    @Column(name = "nutrition")
    private String nutrition;

    @Column(name = "exercise")
    private String exercise;

    @Column(name = "other")
    private String other;

    @Column(name = "development_assessment")
    private String developmentAssessment;

    @JsonIgnoreProperties(value = { "organExamination", "paraclinicalResults", "patient" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "organExamination")
    private MedicalRecord medicalRecord;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrganExamination id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardiovascular() {
        return this.cardiovascular;
    }

    public OrganExamination cardiovascular(String cardiovascular) {
        this.setCardiovascular(cardiovascular);
        return this;
    }

    public void setCardiovascular(String cardiovascular) {
        this.cardiovascular = cardiovascular;
    }

    public String getRespiratory() {
        return this.respiratory;
    }

    public OrganExamination respiratory(String respiratory) {
        this.setRespiratory(respiratory);
        return this;
    }

    public void setRespiratory(String respiratory) {
        this.respiratory = respiratory;
    }

    public String getDigestive() {
        return this.digestive;
    }

    public OrganExamination digestive(String digestive) {
        this.setDigestive(digestive);
        return this;
    }

    public void setDigestive(String digestive) {
        this.digestive = digestive;
    }

    public String getUrinary() {
        return this.urinary;
    }

    public OrganExamination urinary(String urinary) {
        this.setUrinary(urinary);
        return this;
    }

    public void setUrinary(String urinary) {
        this.urinary = urinary;
    }

    public String getMusculoskeletal() {
        return this.musculoskeletal;
    }

    public OrganExamination musculoskeletal(String musculoskeletal) {
        this.setMusculoskeletal(musculoskeletal);
        return this;
    }

    public void setMusculoskeletal(String musculoskeletal) {
        this.musculoskeletal = musculoskeletal;
    }

    public String getEndocrine() {
        return this.endocrine;
    }

    public OrganExamination endocrine(String endocrine) {
        this.setEndocrine(endocrine);
        return this;
    }

    public void setEndocrine(String endocrine) {
        this.endocrine = endocrine;
    }

    public String getNeurological() {
        return this.neurological;
    }

    public OrganExamination neurological(String neurological) {
        this.setNeurological(neurological);
        return this;
    }

    public void setNeurological(String neurological) {
        this.neurological = neurological;
    }

    public String getPsychiatric() {
        return this.psychiatric;
    }

    public OrganExamination psychiatric(String psychiatric) {
        this.setPsychiatric(psychiatric);
        return this;
    }

    public void setPsychiatric(String psychiatric) {
        this.psychiatric = psychiatric;
    }

    public String getSurgery() {
        return this.surgery;
    }

    public OrganExamination surgery(String surgery) {
        this.setSurgery(surgery);
        return this;
    }

    public void setSurgery(String surgery) {
        this.surgery = surgery;
    }

    public String getObstetricsAndGynecology() {
        return this.obstetricsAndGynecology;
    }

    public OrganExamination obstetricsAndGynecology(String obstetricsAndGynecology) {
        this.setObstetricsAndGynecology(obstetricsAndGynecology);
        return this;
    }

    public void setObstetricsAndGynecology(String obstetricsAndGynecology) {
        this.obstetricsAndGynecology = obstetricsAndGynecology;
    }

    public String getOtolaryngology() {
        return this.otolaryngology;
    }

    public OrganExamination otolaryngology(String otolaryngology) {
        this.setOtolaryngology(otolaryngology);
        return this;
    }

    public void setOtolaryngology(String otolaryngology) {
        this.otolaryngology = otolaryngology;
    }

    public String getDentistryAndMaxillofacialSurgery() {
        return this.dentistryAndMaxillofacialSurgery;
    }

    public OrganExamination dentistryAndMaxillofacialSurgery(String dentistryAndMaxillofacialSurgery) {
        this.setDentistryAndMaxillofacialSurgery(dentistryAndMaxillofacialSurgery);
        return this;
    }

    public void setDentistryAndMaxillofacialSurgery(String dentistryAndMaxillofacialSurgery) {
        this.dentistryAndMaxillofacialSurgery = dentistryAndMaxillofacialSurgery;
    }

    public String getEye() {
        return this.eye;
    }

    public OrganExamination eye(String eye) {
        this.setEye(eye);
        return this;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getDermatology() {
        return this.dermatology;
    }

    public OrganExamination dermatology(String dermatology) {
        this.setDermatology(dermatology);
        return this;
    }

    public void setDermatology(String dermatology) {
        this.dermatology = dermatology;
    }

    public String getNutrition() {
        return this.nutrition;
    }

    public OrganExamination nutrition(String nutrition) {
        this.setNutrition(nutrition);
        return this;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getExercise() {
        return this.exercise;
    }

    public OrganExamination exercise(String exercise) {
        this.setExercise(exercise);
        return this;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getOther() {
        return this.other;
    }

    public OrganExamination other(String other) {
        this.setOther(other);
        return this;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDevelopmentAssessment() {
        return this.developmentAssessment;
    }

    public OrganExamination developmentAssessment(String developmentAssessment) {
        this.setDevelopmentAssessment(developmentAssessment);
        return this;
    }

    public void setDevelopmentAssessment(String developmentAssessment) {
        this.developmentAssessment = developmentAssessment;
    }

    public MedicalRecord getMedicalRecord() {
        return this.medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        if (this.medicalRecord != null) {
            this.medicalRecord.setOrganExamination(null);
        }
        if (medicalRecord != null) {
            medicalRecord.setOrganExamination(this);
        }
        this.medicalRecord = medicalRecord;
    }

    public OrganExamination medicalRecord(MedicalRecord medicalRecord) {
        this.setMedicalRecord(medicalRecord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganExamination)) {
            return false;
        }
        return getId() != null && getId().equals(((OrganExamination) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganExamination{" +
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
