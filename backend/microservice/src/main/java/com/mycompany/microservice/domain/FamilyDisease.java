package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A FamilyDisease.
 */
@Entity
@Table(name = "family_disease")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyDisease implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "affected_person", nullable = false)
    private String affectedPerson;

    @Column(name = "relationship_to_patient")
    private String relationshipToPatient;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "familyDisease")
    @JsonIgnoreProperties(value = { "familyDisease" }, allowSetters = true)
    private Set<Disease> diseases = new HashSet<>();

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

    public FamilyDisease id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAffectedPerson() {
        return this.affectedPerson;
    }

    public FamilyDisease affectedPerson(String affectedPerson) {
        this.setAffectedPerson(affectedPerson);
        return this;
    }

    public void setAffectedPerson(String affectedPerson) {
        this.affectedPerson = affectedPerson;
    }

    public String getRelationshipToPatient() {
        return this.relationshipToPatient;
    }

    public FamilyDisease relationshipToPatient(String relationshipToPatient) {
        this.setRelationshipToPatient(relationshipToPatient);
        return this;
    }

    public void setRelationshipToPatient(String relationshipToPatient) {
        this.relationshipToPatient = relationshipToPatient;
    }

    public Set<Disease> getDiseases() {
        return this.diseases;
    }

    public void setDiseases(Set<Disease> diseases) {
        if (this.diseases != null) {
            this.diseases.forEach(i -> i.setFamilyDisease(null));
        }
        if (diseases != null) {
            diseases.forEach(i -> i.setFamilyDisease(this));
        }
        this.diseases = diseases;
    }

    public FamilyDisease diseases(Set<Disease> diseases) {
        this.setDiseases(diseases);
        return this;
    }

    public FamilyDisease addDisease(Disease disease) {
        this.diseases.add(disease);
        disease.setFamilyDisease(this);
        return this;
    }

    public FamilyDisease removeDisease(Disease disease) {
        this.diseases.remove(disease);
        disease.setFamilyDisease(null);
        return this;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public FamilyDisease patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FamilyDisease)) {
            return false;
        }
        return getId() != null && getId().equals(((FamilyDisease) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyDisease{" +
            "id=" + getId() +
            ", affectedPerson='" + getAffectedPerson() + "'" +
            ", relationshipToPatient='" + getRelationshipToPatient() + "'" +
            "}";
    }
}
