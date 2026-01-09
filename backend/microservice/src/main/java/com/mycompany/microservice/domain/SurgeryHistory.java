package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A SurgeryHistory.
 */
@Entity
@Table(name = "surgery_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurgeryHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "body_part")
    private String bodyPart;

    @Column(name = "surgery_year")
    private Integer surgeryYear;

    @Column(name = "note")
    private String note;

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

    public SurgeryHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBodyPart() {
        return this.bodyPart;
    }

    public SurgeryHistory bodyPart(String bodyPart) {
        this.setBodyPart(bodyPart);
        return this;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public Integer getSurgeryYear() {
        return this.surgeryYear;
    }

    public SurgeryHistory surgeryYear(Integer surgeryYear) {
        this.setSurgeryYear(surgeryYear);
        return this;
    }

    public void setSurgeryYear(Integer surgeryYear) {
        this.surgeryYear = surgeryYear;
    }

    public String getNote() {
        return this.note;
    }

    public SurgeryHistory note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public SurgeryHistory patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurgeryHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((SurgeryHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurgeryHistory{" +
            "id=" + getId() +
            ", bodyPart='" + getBodyPart() + "'" +
            ", surgeryYear=" + getSurgeryYear() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
