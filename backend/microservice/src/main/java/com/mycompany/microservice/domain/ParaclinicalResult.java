package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ParaclinicalResult.
 */
@Entity
@Table(name = "paraclinical_result")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParaclinicalResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "result")
    private String result;

    @Column(name = "result_date")
    private LocalDate resultDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "organExamination", "paraclinicalResults", "patient" }, allowSetters = true)
    private MedicalRecord medicalRecord;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParaclinicalResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return this.testName;
    }

    public ParaclinicalResult testName(String testName) {
        this.setTestName(testName);
        return this;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getResult() {
        return this.result;
    }

    public ParaclinicalResult result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDate getResultDate() {
        return this.resultDate;
    }

    public ParaclinicalResult resultDate(LocalDate resultDate) {
        this.setResultDate(resultDate);
        return this;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }

    public MedicalRecord getMedicalRecord() {
        return this.medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public ParaclinicalResult medicalRecord(MedicalRecord medicalRecord) {
        this.setMedicalRecord(medicalRecord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParaclinicalResult)) {
            return false;
        }
        return getId() != null && getId().equals(((ParaclinicalResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParaclinicalResult{" +
            "id=" + getId() +
            ", testName='" + getTestName() + "'" +
            ", result='" + getResult() + "'" +
            ", resultDate='" + getResultDate() + "'" +
            "}";
    }
}
