package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.BirthStatusType;
import com.mycompany.microservice.domain.enumeration.RiskLevel;
import com.mycompany.microservice.domain.enumeration.RiskLevelAlcohol;
import com.mycompany.microservice.domain.enumeration.TypeToilet;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.AdditionalInformation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditionalInformationDTO implements Serializable {

    private Long id;

    private RiskLevel smoking;

    private RiskLevelAlcohol alcoholRiskLevel;

    private Integer alcoholGlassesPerDay;

    private RiskLevel drugUse;

    private RiskLevel physicalActivity;

    private String exposureFactor;

    private LocalDate exposureDate;

    private TypeToilet typeToilet;

    private String environmentalRisk;

    private Boolean cardiovascularDisease;

    private Boolean hypertension;

    private Boolean diabetes;

    private Boolean stomachDisease;

    private Boolean chronicLungDisease;

    private Boolean asthma;

    private Boolean goiter;

    private Boolean hepatitis;

    private Boolean congenitalHeartDisease;

    private Boolean mentalDisorders;

    private Boolean autism;

    private Boolean epilepsy;

    private String cancer;

    private String tuberculosis;

    private String otherDiseases;

    private String contraceptiveMethod;

    private Integer lastPregnancy;

    private Integer numberOfPregnancies;

    private Integer numberOfMiscarriages;

    private Integer numberOfAbortions;

    private Integer numberOfBirths;

    private Integer vaginalDelivery;

    private Integer cesareanSection;

    private Integer difficultDelivery;

    private Integer numberOfFullTermBirths;

    private Integer numberOfPrematureBirths;

    private Integer numberOfChildrenAlive;

    private String gynecologicalDiseases;

    @NotNull
    private BirthStatusType birthStatus;

    private Double birthWeight;

    private Double birthHeight;

    private String birthDefectNote;

    private String otherBirthNote;

    private String otherHealthNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskLevel getSmoking() {
        return smoking;
    }

    public void setSmoking(RiskLevel smoking) {
        this.smoking = smoking;
    }

    public RiskLevelAlcohol getAlcoholRiskLevel() {
        return alcoholRiskLevel;
    }

    public void setAlcoholRiskLevel(RiskLevelAlcohol alcoholRiskLevel) {
        this.alcoholRiskLevel = alcoholRiskLevel;
    }

    public Integer getAlcoholGlassesPerDay() {
        return alcoholGlassesPerDay;
    }

    public void setAlcoholGlassesPerDay(Integer alcoholGlassesPerDay) {
        this.alcoholGlassesPerDay = alcoholGlassesPerDay;
    }

    public RiskLevel getDrugUse() {
        return drugUse;
    }

    public void setDrugUse(RiskLevel drugUse) {
        this.drugUse = drugUse;
    }

    public RiskLevel getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(RiskLevel physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public String getExposureFactor() {
        return exposureFactor;
    }

    public void setExposureFactor(String exposureFactor) {
        this.exposureFactor = exposureFactor;
    }

    public LocalDate getExposureDate() {
        return exposureDate;
    }

    public void setExposureDate(LocalDate exposureDate) {
        this.exposureDate = exposureDate;
    }

    public TypeToilet getTypeToilet() {
        return typeToilet;
    }

    public void setTypeToilet(TypeToilet typeToilet) {
        this.typeToilet = typeToilet;
    }

    public String getEnvironmentalRisk() {
        return environmentalRisk;
    }

    public void setEnvironmentalRisk(String environmentalRisk) {
        this.environmentalRisk = environmentalRisk;
    }

    public Boolean getCardiovascularDisease() {
        return cardiovascularDisease;
    }

    public void setCardiovascularDisease(Boolean cardiovascularDisease) {
        this.cardiovascularDisease = cardiovascularDisease;
    }

    public Boolean getHypertension() {
        return hypertension;
    }

    public void setHypertension(Boolean hypertension) {
        this.hypertension = hypertension;
    }

    public Boolean getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Boolean diabetes) {
        this.diabetes = diabetes;
    }

    public Boolean getStomachDisease() {
        return stomachDisease;
    }

    public void setStomachDisease(Boolean stomachDisease) {
        this.stomachDisease = stomachDisease;
    }

    public Boolean getChronicLungDisease() {
        return chronicLungDisease;
    }

    public void setChronicLungDisease(Boolean chronicLungDisease) {
        this.chronicLungDisease = chronicLungDisease;
    }

    public Boolean getAsthma() {
        return asthma;
    }

    public void setAsthma(Boolean asthma) {
        this.asthma = asthma;
    }

    public Boolean getGoiter() {
        return goiter;
    }

    public void setGoiter(Boolean goiter) {
        this.goiter = goiter;
    }

    public Boolean getHepatitis() {
        return hepatitis;
    }

    public void setHepatitis(Boolean hepatitis) {
        this.hepatitis = hepatitis;
    }

    public Boolean getCongenitalHeartDisease() {
        return congenitalHeartDisease;
    }

    public void setCongenitalHeartDisease(Boolean congenitalHeartDisease) {
        this.congenitalHeartDisease = congenitalHeartDisease;
    }

    public Boolean getMentalDisorders() {
        return mentalDisorders;
    }

    public void setMentalDisorders(Boolean mentalDisorders) {
        this.mentalDisorders = mentalDisorders;
    }

    public Boolean getAutism() {
        return autism;
    }

    public void setAutism(Boolean autism) {
        this.autism = autism;
    }

    public Boolean getEpilepsy() {
        return epilepsy;
    }

    public void setEpilepsy(Boolean epilepsy) {
        this.epilepsy = epilepsy;
    }

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public String getTuberculosis() {
        return tuberculosis;
    }

    public void setTuberculosis(String tuberculosis) {
        this.tuberculosis = tuberculosis;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public String getContraceptiveMethod() {
        return contraceptiveMethod;
    }

    public void setContraceptiveMethod(String contraceptiveMethod) {
        this.contraceptiveMethod = contraceptiveMethod;
    }

    public Integer getLastPregnancy() {
        return lastPregnancy;
    }

    public void setLastPregnancy(Integer lastPregnancy) {
        this.lastPregnancy = lastPregnancy;
    }

    public Integer getNumberOfPregnancies() {
        return numberOfPregnancies;
    }

    public void setNumberOfPregnancies(Integer numberOfPregnancies) {
        this.numberOfPregnancies = numberOfPregnancies;
    }

    public Integer getNumberOfMiscarriages() {
        return numberOfMiscarriages;
    }

    public void setNumberOfMiscarriages(Integer numberOfMiscarriages) {
        this.numberOfMiscarriages = numberOfMiscarriages;
    }

    public Integer getNumberOfAbortions() {
        return numberOfAbortions;
    }

    public void setNumberOfAbortions(Integer numberOfAbortions) {
        this.numberOfAbortions = numberOfAbortions;
    }

    public Integer getNumberOfBirths() {
        return numberOfBirths;
    }

    public void setNumberOfBirths(Integer numberOfBirths) {
        this.numberOfBirths = numberOfBirths;
    }

    public Integer getVaginalDelivery() {
        return vaginalDelivery;
    }

    public void setVaginalDelivery(Integer vaginalDelivery) {
        this.vaginalDelivery = vaginalDelivery;
    }

    public Integer getCesareanSection() {
        return cesareanSection;
    }

    public void setCesareanSection(Integer cesareanSection) {
        this.cesareanSection = cesareanSection;
    }

    public Integer getDifficultDelivery() {
        return difficultDelivery;
    }

    public void setDifficultDelivery(Integer difficultDelivery) {
        this.difficultDelivery = difficultDelivery;
    }

    public Integer getNumberOfFullTermBirths() {
        return numberOfFullTermBirths;
    }

    public void setNumberOfFullTermBirths(Integer numberOfFullTermBirths) {
        this.numberOfFullTermBirths = numberOfFullTermBirths;
    }

    public Integer getNumberOfPrematureBirths() {
        return numberOfPrematureBirths;
    }

    public void setNumberOfPrematureBirths(Integer numberOfPrematureBirths) {
        this.numberOfPrematureBirths = numberOfPrematureBirths;
    }

    public Integer getNumberOfChildrenAlive() {
        return numberOfChildrenAlive;
    }

    public void setNumberOfChildrenAlive(Integer numberOfChildrenAlive) {
        this.numberOfChildrenAlive = numberOfChildrenAlive;
    }

    public String getGynecologicalDiseases() {
        return gynecologicalDiseases;
    }

    public void setGynecologicalDiseases(String gynecologicalDiseases) {
        this.gynecologicalDiseases = gynecologicalDiseases;
    }

    public BirthStatusType getBirthStatus() {
        return birthStatus;
    }

    public void setBirthStatus(BirthStatusType birthStatus) {
        this.birthStatus = birthStatus;
    }

    public Double getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(Double birthWeight) {
        this.birthWeight = birthWeight;
    }

    public Double getBirthHeight() {
        return birthHeight;
    }

    public void setBirthHeight(Double birthHeight) {
        this.birthHeight = birthHeight;
    }

    public String getBirthDefectNote() {
        return birthDefectNote;
    }

    public void setBirthDefectNote(String birthDefectNote) {
        this.birthDefectNote = birthDefectNote;
    }

    public String getOtherBirthNote() {
        return otherBirthNote;
    }

    public void setOtherBirthNote(String otherBirthNote) {
        this.otherBirthNote = otherBirthNote;
    }

    public String getOtherHealthNote() {
        return otherHealthNote;
    }

    public void setOtherHealthNote(String otherHealthNote) {
        this.otherHealthNote = otherHealthNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalInformationDTO)) {
            return false;
        }

        AdditionalInformationDTO additionalInformationDTO = (AdditionalInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, additionalInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditionalInformationDTO{" +
            "id=" + getId() +
            ", smoking='" + getSmoking() + "'" +
            ", alcoholRiskLevel='" + getAlcoholRiskLevel() + "'" +
            ", alcoholGlassesPerDay=" + getAlcoholGlassesPerDay() +
            ", drugUse='" + getDrugUse() + "'" +
            ", physicalActivity='" + getPhysicalActivity() + "'" +
            ", exposureFactor='" + getExposureFactor() + "'" +
            ", exposureDate='" + getExposureDate() + "'" +
            ", typeToilet='" + getTypeToilet() + "'" +
            ", environmentalRisk='" + getEnvironmentalRisk() + "'" +
            ", cardiovascularDisease='" + getCardiovascularDisease() + "'" +
            ", hypertension='" + getHypertension() + "'" +
            ", diabetes='" + getDiabetes() + "'" +
            ", stomachDisease='" + getStomachDisease() + "'" +
            ", chronicLungDisease='" + getChronicLungDisease() + "'" +
            ", asthma='" + getAsthma() + "'" +
            ", goiter='" + getGoiter() + "'" +
            ", hepatitis='" + getHepatitis() + "'" +
            ", congenitalHeartDisease='" + getCongenitalHeartDisease() + "'" +
            ", mentalDisorders='" + getMentalDisorders() + "'" +
            ", autism='" + getAutism() + "'" +
            ", epilepsy='" + getEpilepsy() + "'" +
            ", cancer='" + getCancer() + "'" +
            ", tuberculosis='" + getTuberculosis() + "'" +
            ", otherDiseases='" + getOtherDiseases() + "'" +
            ", contraceptiveMethod='" + getContraceptiveMethod() + "'" +
            ", lastPregnancy=" + getLastPregnancy() +
            ", numberOfPregnancies=" + getNumberOfPregnancies() +
            ", numberOfMiscarriages=" + getNumberOfMiscarriages() +
            ", numberOfAbortions=" + getNumberOfAbortions() +
            ", numberOfBirths=" + getNumberOfBirths() +
            ", vaginalDelivery=" + getVaginalDelivery() +
            ", cesareanSection=" + getCesareanSection() +
            ", difficultDelivery=" + getDifficultDelivery() +
            ", numberOfFullTermBirths=" + getNumberOfFullTermBirths() +
            ", numberOfPrematureBirths=" + getNumberOfPrematureBirths() +
            ", numberOfChildrenAlive=" + getNumberOfChildrenAlive() +
            ", gynecologicalDiseases='" + getGynecologicalDiseases() + "'" +
            ", birthStatus='" + getBirthStatus() + "'" +
            ", birthWeight=" + getBirthWeight() +
            ", birthHeight=" + getBirthHeight() +
            ", birthDefectNote='" + getBirthDefectNote() + "'" +
            ", otherBirthNote='" + getOtherBirthNote() + "'" +
            ", otherHealthNote='" + getOtherHealthNote() + "'" +
            "}";
    }
}
