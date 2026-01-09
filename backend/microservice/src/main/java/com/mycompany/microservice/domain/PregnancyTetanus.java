package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.TetanusDose;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PregnancyTetanus.
 */
@Entity
@Table(name = "pregnancy_tetanus")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PregnancyTetanus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dose", nullable = false)
    private TetanusDose dose;

    @Column(name = "not_injected")
    private Boolean notInjected;

    @Column(name = "injection_date")
    private LocalDate injectionDate;

    @Column(name = "pregnancy_month")
    private Integer pregnancyMonth;

    @Column(name = "reaction")
    private String reaction;

    @Column(name = "next_appointment")
    private LocalDate nextAppointment;

    @Column(name = "number_of_doses_received")
    private Integer numberOfDosesReceived;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public PregnancyTetanus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TetanusDose getDose() {
        return this.dose;
    }

    public PregnancyTetanus dose(TetanusDose dose) {
        this.setDose(dose);
        return this;
    }

    public void setDose(TetanusDose dose) {
        this.dose = dose;
    }

    public Boolean getNotInjected() {
        return this.notInjected;
    }

    public PregnancyTetanus notInjected(Boolean notInjected) {
        this.setNotInjected(notInjected);
        return this;
    }

    public void setNotInjected(Boolean notInjected) {
        this.notInjected = notInjected;
    }

    public LocalDate getInjectionDate() {
        return this.injectionDate;
    }

    public PregnancyTetanus injectionDate(LocalDate injectionDate) {
        this.setInjectionDate(injectionDate);
        return this;
    }

    public void setInjectionDate(LocalDate injectionDate) {
        this.injectionDate = injectionDate;
    }

    public Integer getPregnancyMonth() {
        return this.pregnancyMonth;
    }

    public PregnancyTetanus pregnancyMonth(Integer pregnancyMonth) {
        this.setPregnancyMonth(pregnancyMonth);
        return this;
    }

    public void setPregnancyMonth(Integer pregnancyMonth) {
        this.pregnancyMonth = pregnancyMonth;
    }

    public String getReaction() {
        return this.reaction;
    }

    public PregnancyTetanus reaction(String reaction) {
        this.setReaction(reaction);
        return this;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public LocalDate getNextAppointment() {
        return this.nextAppointment;
    }

    public PregnancyTetanus nextAppointment(LocalDate nextAppointment) {
        this.setNextAppointment(nextAppointment);
        return this;
    }

    public void setNextAppointment(LocalDate nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public Integer getNumberOfDosesReceived() {
        return this.numberOfDosesReceived;
    }

    public PregnancyTetanus numberOfDosesReceived(Integer numberOfDosesReceived) {
        this.setNumberOfDosesReceived(numberOfDosesReceived);
        return this;
    }

    public void setNumberOfDosesReceived(Integer numberOfDosesReceived) {
        this.numberOfDosesReceived = numberOfDosesReceived;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PregnancyTetanus patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PregnancyTetanus)) {
            return false;
        }
        return getId() != null && getId().equals(((PregnancyTetanus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PregnancyTetanus{" +
            "id=" + getId() +
            ", dose='" + getDose() + "'" +
            ", notInjected='" + getNotInjected() + "'" +
            ", injectionDate='" + getInjectionDate() + "'" +
            ", pregnancyMonth=" + getPregnancyMonth() +
            ", reaction='" + getReaction() + "'" +
            ", nextAppointment='" + getNextAppointment() + "'" +
            ", numberOfDosesReceived=" + getNumberOfDosesReceived() +
            "}";
    }
}
