package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.DisabilityType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.Disability} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisabilityDTO implements Serializable {

    private Long id;

    @NotNull
    private DisabilityType type;

    private String description;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DisabilityType getType() {
        return type;
    }

    public void setType(DisabilityType type) {
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
        if (!(o instanceof DisabilityDTO)) {
            return false;
        }

        DisabilityDTO disabilityDTO = (DisabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, disabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisabilityDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
