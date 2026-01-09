package com.mycompany.microservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.FamilyDisease} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyDiseaseDTO implements Serializable {

    private Long id;

    @NotNull
    private String affectedPerson;

    private String relationshipToPatient;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAffectedPerson() {
        return affectedPerson;
    }

    public void setAffectedPerson(String affectedPerson) {
        this.affectedPerson = affectedPerson;
    }

    public String getRelationshipToPatient() {
        return relationshipToPatient;
    }

    public void setRelationshipToPatient(String relationshipToPatient) {
        this.relationshipToPatient = relationshipToPatient;
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
        if (!(o instanceof FamilyDiseaseDTO)) {
            return false;
        }

        FamilyDiseaseDTO familyDiseaseDTO = (FamilyDiseaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, familyDiseaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyDiseaseDTO{" +
            "id=" + getId() +
            ", affectedPerson='" + getAffectedPerson() + "'" +
            ", relationshipToPatient='" + getRelationshipToPatient() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
