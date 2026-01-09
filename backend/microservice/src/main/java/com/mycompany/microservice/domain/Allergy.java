package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.AllergyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Allergy.
 */
@Entity
@Table(name = "allergy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Allergy implements Serializable {

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

    public Allergy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllergyType getType() {
        return this.type;
    }

    public Allergy type(AllergyType type) {
        this.setType(type);
        return this;
    }

    public void setType(AllergyType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Allergy description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Allergy patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Allergy)) {
            return false;
        }
        return getId() != null && getId().equals(((Allergy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Allergy{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
