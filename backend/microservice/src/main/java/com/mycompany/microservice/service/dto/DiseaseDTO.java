package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.DiseaseName;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.Disease} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiseaseDTO implements Serializable {

    private Long id;

    @NotNull
    private DiseaseName name;

    private String specificType;

    private String description;

    @NotNull
    private FamilyDiseaseDTO familyDisease;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiseaseName getName() {
        return name;
    }

    public void setName(DiseaseName name) {
        this.name = name;
    }

    public String getSpecificType() {
        return specificType;
    }

    public void setSpecificType(String specificType) {
        this.specificType = specificType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FamilyDiseaseDTO getFamilyDisease() {
        return familyDisease;
    }

    public void setFamilyDisease(FamilyDiseaseDTO familyDisease) {
        this.familyDisease = familyDisease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiseaseDTO)) {
            return false;
        }

        DiseaseDTO diseaseDTO = (DiseaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, diseaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiseaseDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", specificType='" + getSpecificType() + "'" +
            ", description='" + getDescription() + "'" +
            ", familyDisease=" + getFamilyDisease() +
            "}";
    }
}
