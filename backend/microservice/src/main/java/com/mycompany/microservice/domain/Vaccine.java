package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.VaccineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Vaccine.
 */
@Entity
@Table(name = "vaccine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vaccine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private VaccineType name;

    @Column(name = "not_vaccinated")
    private Boolean notVaccinated;

    @Column(name = "injection_date")
    private LocalDate injectionDate;

    @Column(name = "reaction")
    private String reaction;

    @Column(name = "next_appointment")
    private LocalDate nextAppointment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vaccines", "patient" }, allowSetters = true)
    private VaccinationForBaby vaccinationForBaby;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vaccine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineType getName() {
        return this.name;
    }

    public Vaccine name(VaccineType name) {
        this.setName(name);
        return this;
    }

    public void setName(VaccineType name) {
        this.name = name;
    }

    public Boolean getNotVaccinated() {
        return this.notVaccinated;
    }

    public Vaccine notVaccinated(Boolean notVaccinated) {
        this.setNotVaccinated(notVaccinated);
        return this;
    }

    public void setNotVaccinated(Boolean notVaccinated) {
        this.notVaccinated = notVaccinated;
    }

    public LocalDate getInjectionDate() {
        return this.injectionDate;
    }

    public Vaccine injectionDate(LocalDate injectionDate) {
        this.setInjectionDate(injectionDate);
        return this;
    }

    public void setInjectionDate(LocalDate injectionDate) {
        this.injectionDate = injectionDate;
    }

    public String getReaction() {
        return this.reaction;
    }

    public Vaccine reaction(String reaction) {
        this.setReaction(reaction);
        return this;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public LocalDate getNextAppointment() {
        return this.nextAppointment;
    }

    public Vaccine nextAppointment(LocalDate nextAppointment) {
        this.setNextAppointment(nextAppointment);
        return this;
    }

    public void setNextAppointment(LocalDate nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public VaccinationForBaby getVaccinationForBaby() {
        return this.vaccinationForBaby;
    }

    public void setVaccinationForBaby(VaccinationForBaby vaccinationForBaby) {
        this.vaccinationForBaby = vaccinationForBaby;
    }

    public Vaccine vaccinationForBaby(VaccinationForBaby vaccinationForBaby) {
        this.setVaccinationForBaby(vaccinationForBaby);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaccine)) {
            return false;
        }
        return getId() != null && getId().equals(((Vaccine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaccine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", notVaccinated='" + getNotVaccinated() + "'" +
            ", injectionDate='" + getInjectionDate() + "'" +
            ", reaction='" + getReaction() + "'" +
            ", nextAppointment='" + getNextAppointment() + "'" +
            "}";
    }
}
