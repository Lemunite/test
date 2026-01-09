package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.VaccineType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.Vaccine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccineDTO implements Serializable {

    private Long id;

    @NotNull
    private VaccineType name;

    private Boolean notVaccinated;

    private LocalDate injectionDate;

    private String reaction;

    private LocalDate nextAppointment;

    @NotNull
    private VaccinationForBabyDTO vaccinationForBaby;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineType getName() {
        return name;
    }

    public void setName(VaccineType name) {
        this.name = name;
    }

    public Boolean getNotVaccinated() {
        return notVaccinated;
    }

    public void setNotVaccinated(Boolean notVaccinated) {
        this.notVaccinated = notVaccinated;
    }

    public LocalDate getInjectionDate() {
        return injectionDate;
    }

    public void setInjectionDate(LocalDate injectionDate) {
        this.injectionDate = injectionDate;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public LocalDate getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(LocalDate nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public VaccinationForBabyDTO getVaccinationForBaby() {
        return vaccinationForBaby;
    }

    public void setVaccinationForBaby(VaccinationForBabyDTO vaccinationForBaby) {
        this.vaccinationForBaby = vaccinationForBaby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccineDTO)) {
            return false;
        }

        VaccineDTO vaccineDTO = (VaccineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", notVaccinated='" + getNotVaccinated() + "'" +
            ", injectionDate='" + getInjectionDate() + "'" +
            ", reaction='" + getReaction() + "'" +
            ", nextAppointment='" + getNextAppointment() + "'" +
            ", vaccinationForBaby=" + getVaccinationForBaby() +
            "}";
    }
}
