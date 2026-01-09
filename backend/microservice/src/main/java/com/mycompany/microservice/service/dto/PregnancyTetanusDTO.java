package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.TetanusDose;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.PregnancyTetanus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PregnancyTetanusDTO implements Serializable {

    private Long id;

    @NotNull
    private TetanusDose dose;

    private Boolean notInjected;

    private LocalDate injectionDate;

    private Integer pregnancyMonth;

    private String reaction;

    private LocalDate nextAppointment;

    private Integer numberOfDosesReceived;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TetanusDose getDose() {
        return dose;
    }

    public void setDose(TetanusDose dose) {
        this.dose = dose;
    }

    public Boolean getNotInjected() {
        return notInjected;
    }

    public void setNotInjected(Boolean notInjected) {
        this.notInjected = notInjected;
    }

    public LocalDate getInjectionDate() {
        return injectionDate;
    }

    public void setInjectionDate(LocalDate injectionDate) {
        this.injectionDate = injectionDate;
    }

    public Integer getPregnancyMonth() {
        return pregnancyMonth;
    }

    public void setPregnancyMonth(Integer pregnancyMonth) {
        this.pregnancyMonth = pregnancyMonth;
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

    public Integer getNumberOfDosesReceived() {
        return numberOfDosesReceived;
    }

    public void setNumberOfDosesReceived(Integer numberOfDosesReceived) {
        this.numberOfDosesReceived = numberOfDosesReceived;
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
        if (!(o instanceof PregnancyTetanusDTO)) {
            return false;
        }

        PregnancyTetanusDTO pregnancyTetanusDTO = (PregnancyTetanusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pregnancyTetanusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PregnancyTetanusDTO{" +
            "id=" + getId() +
            ", dose='" + getDose() + "'" +
            ", notInjected='" + getNotInjected() + "'" +
            ", injectionDate='" + getInjectionDate() + "'" +
            ", pregnancyMonth=" + getPregnancyMonth() +
            ", reaction='" + getReaction() + "'" +
            ", nextAppointment='" + getNextAppointment() + "'" +
            ", numberOfDosesReceived=" + getNumberOfDosesReceived() +
            ", patient=" + getPatient() +
            "}";
    }
}
