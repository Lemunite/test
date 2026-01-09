package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.AllergyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A FamilyAllergy.
 */
@Entity
@Table(name = "family_allergy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyAllergy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AllergyType type;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "affected_person", nullable = false)
    private String affectedPerson;

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

    public FamilyAllergy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllergyType getType() {
        return this.type;
    }

    public FamilyAllergy type(AllergyType type) {
        this.setType(type);
        return this;
    }

    public void setType(AllergyType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public FamilyAllergy description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAffectedPerson() {
        return this.affectedPerson;
    }

    public FamilyAllergy affectedPerson(String affectedPerson) {
        this.setAffectedPerson(affectedPerson);
        return this;
    }

    public void setAffectedPerson(String affectedPerson) {
        this.affectedPerson = affectedPerson;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public FamilyAllergy patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FamilyAllergy)) {
            return false;
        }
        return getId() != null && getId().equals(((FamilyAllergy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyAllergy{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", affectedPerson='" + getAffectedPerson() + "'" +
            "}";
    }
}
