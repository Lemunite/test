package com.mycompany.microservice.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.ParaclinicalResult} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParaclinicalResultDTO implements Serializable {

    private Long id;

    @NotNull
    private String testName;

    @Lob
    private String result;

    private LocalDate resultDate;

    @NotNull
    private MedicalRecordDTO medicalRecord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }

    public MedicalRecordDTO getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecordDTO medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParaclinicalResultDTO)) {
            return false;
        }

        ParaclinicalResultDTO paraclinicalResultDTO = (ParaclinicalResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paraclinicalResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParaclinicalResultDTO{" +
            "id=" + getId() +
            ", testName='" + getTestName() + "'" +
            ", result='" + getResult() + "'" +
            ", resultDate='" + getResultDate() + "'" +
            ", medicalRecord=" + getMedicalRecord() +
            "}";
    }
}
