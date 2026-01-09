package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.VaccineTCMRType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A VaccinationTCMR.
 */
@Entity
@Table(name = "vaccination_tcmr")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccinationTCMR implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vaccine", nullable = false)
    private VaccineTCMRType vaccine;

    @Column(name = "not_vaccinated")
    private Boolean notVaccinated;

    @Column(name = "vaccinated")
    private Boolean vaccinated;

    @Column(name = "injection_date")
    private LocalDate injectionDate;

    @Column(name = "reaction")
    private String reaction;

    @Column(name = "next_appointment")
    private LocalDate nextAppointment;

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

    public VaccinationTCMR id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineTCMRType getVaccine() {
        return this.vaccine;
    }

    public VaccinationTCMR vaccine(VaccineTCMRType vaccine) {
        this.setVaccine(vaccine);
        return this;
    }

    public void setVaccine(VaccineTCMRType vaccine) {
        this.vaccine = vaccine;
    }

    public Boolean getNotVaccinated() {
        return this.notVaccinated;
    }

    public VaccinationTCMR notVaccinated(Boolean notVaccinated) {
        this.setNotVaccinated(notVaccinated);
        return this;
    }

    public void setNotVaccinated(Boolean notVaccinated) {
        this.notVaccinated = notVaccinated;
    }

    public Boolean getVaccinated() {
        return this.vaccinated;
    }

    public VaccinationTCMR vaccinated(Boolean vaccinated) {
        this.setVaccinated(vaccinated);
        return this;
    }

    public void setVaccinated(Boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public LocalDate getInjectionDate() {
        return this.injectionDate;
    }

    public VaccinationTCMR injectionDate(LocalDate injectionDate) {
        this.setInjectionDate(injectionDate);
        return this;
    }

    public void setInjectionDate(LocalDate injectionDate) {
        this.injectionDate = injectionDate;
    }

    public String getReaction() {
        return this.reaction;
    }

    public VaccinationTCMR reaction(String reaction) {
        this.setReaction(reaction);
        return this;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public LocalDate getNextAppointment() {
        return this.nextAppointment;
    }

    public VaccinationTCMR nextAppointment(LocalDate nextAppointment) {
        this.setNextAppointment(nextAppointment);
        return this;
    }

    public void setNextAppointment(LocalDate nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public VaccinationTCMR patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationTCMR)) {
            return false;
        }
        return getId() != null && getId().equals(((VaccinationTCMR) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationTCMR{" +
            "id=" + getId() +
            ", vaccine='" + getVaccine() + "'" +
            ", notVaccinated='" + getNotVaccinated() + "'" +
            ", vaccinated='" + getVaccinated() + "'" +
            ", injectionDate='" + getInjectionDate() + "'" +
            ", reaction='" + getReaction() + "'" +
            ", nextAppointment='" + getNextAppointment() + "'" +
            "}";
    }
}
