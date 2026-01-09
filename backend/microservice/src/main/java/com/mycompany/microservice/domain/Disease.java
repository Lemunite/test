package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.DiseaseName;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Disease.
 */
@Entity
@Table(name = "disease")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disease implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private DiseaseName name;

    @Column(name = "specific_type")
    private String specificType;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "diseases", "patient" }, allowSetters = true)
    private FamilyDisease familyDisease;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Disease id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiseaseName getName() {
        return this.name;
    }

    public Disease name(DiseaseName name) {
        this.setName(name);
        return this;
    }

    public void setName(DiseaseName name) {
        this.name = name;
    }

    public String getSpecificType() {
        return this.specificType;
    }

    public Disease specificType(String specificType) {
        this.setSpecificType(specificType);
        return this;
    }

    public void setSpecificType(String specificType) {
        this.specificType = specificType;
    }

    public String getDescription() {
        return this.description;
    }

    public Disease description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FamilyDisease getFamilyDisease() {
        return this.familyDisease;
    }

    public void setFamilyDisease(FamilyDisease familyDisease) {
        this.familyDisease = familyDisease;
    }

    public Disease familyDisease(FamilyDisease familyDisease) {
        this.setFamilyDisease(familyDisease);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disease)) {
            return false;
        }
        return getId() != null && getId().equals(((Disease) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disease{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", specificType='" + getSpecificType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
