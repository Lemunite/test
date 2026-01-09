package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.VaccineType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.VaccinationForBaby} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccinationForBabyDTO implements Serializable {

    private Long id;

    private VaccineType vaccine;

    private Integer numberUse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineType getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineType vaccine) {
        this.vaccine = vaccine;
    }

    public Integer getNumberUse() {
        return numberUse;
    }

    public void setNumberUse(Integer numberUse) {
        this.numberUse = numberUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationForBabyDTO)) {
            return false;
        }

        VaccinationForBabyDTO vaccinationForBabyDTO = (VaccinationForBabyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccinationForBabyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationForBabyDTO{" +
            "id=" + getId() +
            ", vaccine='" + getVaccine() + "'" +
            ", numberUse=" + getNumberUse() +
            "}";
    }
}
