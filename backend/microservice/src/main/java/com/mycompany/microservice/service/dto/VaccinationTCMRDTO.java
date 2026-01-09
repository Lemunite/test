package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.VaccineTCMRType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.VaccinationTCMR} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccinationTCMRDTO implements Serializable {

    private Long id;

    @NotNull
    private VaccineTCMRType vaccine;

    private Boolean notVaccinated;

    private Boolean vaccinated;

    private LocalDate injectionDate;

    private String reaction;

    private LocalDate nextAppointment;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineTCMRType getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineTCMRType vaccine) {
        this.vaccine = vaccine;
    }

    public Boolean getNotVaccinated() {
        return notVaccinated;
    }

    public void setNotVaccinated(Boolean notVaccinated) {
        this.notVaccinated = notVaccinated;
    }

    public Boolean getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(Boolean vaccinated) {
        this.vaccinated = vaccinated;
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
        if (!(o instanceof VaccinationTCMRDTO)) {
            return false;
        }

        VaccinationTCMRDTO vaccinationTCMRDTO = (VaccinationTCMRDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccinationTCMRDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationTCMRDTO{" +
            "id=" + getId() +
            ", vaccine='" + getVaccine() + "'" +
            ", notVaccinated='" + getNotVaccinated() + "'" +
            ", vaccinated='" + getVaccinated() + "'" +
            ", injectionDate='" + getInjectionDate() + "'" +
            ", reaction='" + getReaction() + "'" +
            ", nextAppointment='" + getNextAppointment() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
