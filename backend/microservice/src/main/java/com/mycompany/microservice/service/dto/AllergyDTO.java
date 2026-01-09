package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.AllergyType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.Allergy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllergyDTO implements Serializable {

    private Long id;

    @NotNull
    private AllergyType type;

    private String description;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllergyType getType() {
        return type;
    }

    public void setType(AllergyType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof AllergyDTO)) {
            return false;
        }

        AllergyDTO allergyDTO = (AllergyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, allergyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllergyDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
