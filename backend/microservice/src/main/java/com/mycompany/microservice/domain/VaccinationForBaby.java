package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.VaccineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A VaccinationForBaby.
 */
@Entity
@Table(name = "vaccination_for_baby")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccinationForBaby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaccine")
    private VaccineType vaccine;

    @Column(name = "number_use")
    private Integer numberUse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccinationForBaby")
    @JsonIgnoreProperties(value = { "vaccinationForBaby" }, allowSetters = true)
    private Set<Vaccine> vaccines = new HashSet<>();

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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "vaccinationsForBaby")
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VaccinationForBaby id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineType getVaccine() {
        return this.vaccine;
    }

    public VaccinationForBaby vaccine(VaccineType vaccine) {
        this.setVaccine(vaccine);
        return this;
    }

    public void setVaccine(VaccineType vaccine) {
        this.vaccine = vaccine;
    }

    public Integer getNumberUse() {
        return this.numberUse;
    }

    public VaccinationForBaby numberUse(Integer numberUse) {
        this.setNumberUse(numberUse);
        return this;
    }

    public void setNumberUse(Integer numberUse) {
        this.numberUse = numberUse;
    }

    public Set<Vaccine> getVaccines() {
        return this.vaccines;
    }

    public void setVaccines(Set<Vaccine> vaccines) {
        if (this.vaccines != null) {
            this.vaccines.forEach(i -> i.setVaccinationForBaby(null));
        }
        if (vaccines != null) {
            vaccines.forEach(i -> i.setVaccinationForBaby(this));
        }
        this.vaccines = vaccines;
    }

    public VaccinationForBaby vaccines(Set<Vaccine> vaccines) {
        this.setVaccines(vaccines);
        return this;
    }

    public VaccinationForBaby addVaccine(Vaccine vaccine) {
        this.vaccines.add(vaccine);
        vaccine.setVaccinationForBaby(this);
        return this;
    }

    public VaccinationForBaby removeVaccine(Vaccine vaccine) {
        this.vaccines.remove(vaccine);
        vaccine.setVaccinationForBaby(null);
        return this;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        if (this.patient != null) {
            this.patient.setVaccinationsForBaby(null);
        }
        if (patient != null) {
            patient.setVaccinationsForBaby(this);
        }
        this.patient = patient;
    }

    public VaccinationForBaby patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationForBaby)) {
            return false;
        }
        return getId() != null && getId().equals(((VaccinationForBaby) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationForBaby{" +
            "id=" + getId() +
            ", vaccine='" + getVaccine() + "'" +
            ", numberUse=" + getNumberUse() +
            "}";
    }
}
