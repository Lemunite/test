package com.mycompany.microservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.SurgeryHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurgeryHistoryDTO implements Serializable {

    private Long id;

    private String bodyPart;

    private Integer surgeryYear;

    private String note;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public Integer getSurgeryYear() {
        return surgeryYear;
    }

    public void setSurgeryYear(Integer surgeryYear) {
        this.surgeryYear = surgeryYear;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(o instanceof SurgeryHistoryDTO)) {
            return false;
        }

        SurgeryHistoryDTO surgeryHistoryDTO = (SurgeryHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, surgeryHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurgeryHistoryDTO{" +
            "id=" + getId() +
            ", bodyPart='" + getBodyPart() + "'" +
            ", surgeryYear=" + getSurgeryYear() +
            ", note='" + getNote() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
