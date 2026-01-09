package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.BirthStatusType;
import com.mycompany.microservice.domain.enumeration.RiskLevel;
import com.mycompany.microservice.domain.enumeration.RiskLevelAlcohol;
import com.mycompany.microservice.domain.enumeration.TypeToilet;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A AdditionalInformation.
 */
@Entity
@Table(name = "additional_information")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditionalInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "smoking")
    private RiskLevel smoking;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_risk_level")
    private RiskLevelAlcohol alcoholRiskLevel;

    @Column(name = "alcohol_glasses_per_day")
    private Integer alcoholGlassesPerDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "drug_use")
    private RiskLevel drugUse;

    @Enumerated(EnumType.STRING)
    @Column(name = "physical_activity")
    private RiskLevel physicalActivity;

    @Column(name = "exposure_factor")
    private String exposureFactor;

    @Column(name = "exposure_date")
    private LocalDate exposureDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_toilet")
    private TypeToilet typeToilet;

    @Column(name = "environmental_risk")
    private String environmentalRisk;

    @Column(name = "cardiovascular_disease")
    private Boolean cardiovascularDisease;

    @Column(name = "hypertension")
    private Boolean hypertension;

    @Column(name = "diabetes")
    private Boolean diabetes;

    @Column(name = "stomach_disease")
    private Boolean stomachDisease;

    @Column(name = "chronic_lung_disease")
    private Boolean chronicLungDisease;

    @Column(name = "asthma")
    private Boolean asthma;

    @Column(name = "goiter")
    private Boolean goiter;

    @Column(name = "hepatitis")
    private Boolean hepatitis;

    @Column(name = "congenital_heart_disease")
    private Boolean congenitalHeartDisease;

    @Column(name = "mental_disorders")
    private Boolean mentalDisorders;

    @Column(name = "autism")
    private Boolean autism;

    @Column(name = "epilepsy")
    private Boolean epilepsy;

    @Column(name = "cancer")
    private String cancer;

    @Column(name = "tuberculosis")
    private String tuberculosis;

    @Column(name = "other_diseases")
    private String otherDiseases;

    @Column(name = "contraceptive_method")
    private String contraceptiveMethod;

    @Column(name = "last_pregnancy")
    private Integer lastPregnancy;

    @Column(name = "number_of_pregnancies")
    private Integer numberOfPregnancies;

    @Column(name = "number_of_miscarriages")
    private Integer numberOfMiscarriages;

    @Column(name = "number_of_abortions")
    private Integer numberOfAbortions;

    @Column(name = "number_of_births")
    private Integer numberOfBirths;

    @Column(name = "vaginal_delivery")
    private Integer vaginalDelivery;

    @Column(name = "cesarean_section")
    private Integer cesareanSection;

    @Column(name = "difficult_delivery")
    private Integer difficultDelivery;

    @Column(name = "number_of_full_term_births")
    private Integer numberOfFullTermBirths;

    @Column(name = "number_of_premature_births")
    private Integer numberOfPrematureBirths;

    @Column(name = "number_of_children_alive")
    private Integer numberOfChildrenAlive;

    @Column(name = "gynecological_diseases")
    private String gynecologicalDiseases;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "birth_status", nullable = false)
    private BirthStatusType birthStatus;

    @Column(name = "birth_weight")
    private Double birthWeight;

    @Column(name = "birth_height")
    private Double birthHeight;

    @Column(name = "birth_defect_note")
    private String birthDefectNote;

    @Column(name = "other_birth_note")
    private String otherBirthNote;

    @Column(name = "other_health_note")
    private String otherHealthNote;

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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "additionalInfo")
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdditionalInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RiskLevel getSmoking() {
        return this.smoking;
    }

    public AdditionalInformation smoking(RiskLevel smoking) {
        this.setSmoking(smoking);
        return this;
    }

    public void setSmoking(RiskLevel smoking) {
        this.smoking = smoking;
    }

    public RiskLevelAlcohol getAlcoholRiskLevel() {
        return this.alcoholRiskLevel;
    }

    public AdditionalInformation alcoholRiskLevel(RiskLevelAlcohol alcoholRiskLevel) {
        this.setAlcoholRiskLevel(alcoholRiskLevel);
        return this;
    }

    public void setAlcoholRiskLevel(RiskLevelAlcohol alcoholRiskLevel) {
        this.alcoholRiskLevel = alcoholRiskLevel;
    }

    public Integer getAlcoholGlassesPerDay() {
        return this.alcoholGlassesPerDay;
    }

    public AdditionalInformation alcoholGlassesPerDay(Integer alcoholGlassesPerDay) {
        this.setAlcoholGlassesPerDay(alcoholGlassesPerDay);
        return this;
    }

    public void setAlcoholGlassesPerDay(Integer alcoholGlassesPerDay) {
        this.alcoholGlassesPerDay = alcoholGlassesPerDay;
    }

    public RiskLevel getDrugUse() {
        return this.drugUse;
    }

    public AdditionalInformation drugUse(RiskLevel drugUse) {
        this.setDrugUse(drugUse);
        return this;
    }

    public void setDrugUse(RiskLevel drugUse) {
        this.drugUse = drugUse;
    }

    public RiskLevel getPhysicalActivity() {
        return this.physicalActivity;
    }

    public AdditionalInformation physicalActivity(RiskLevel physicalActivity) {
        this.setPhysicalActivity(physicalActivity);
        return this;
    }

    public void setPhysicalActivity(RiskLevel physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public String getExposureFactor() {
        return this.exposureFactor;
    }

    public AdditionalInformation exposureFactor(String exposureFactor) {
        this.setExposureFactor(exposureFactor);
        return this;
    }

    public void setExposureFactor(String exposureFactor) {
        this.exposureFactor = exposureFactor;
    }

    public LocalDate getExposureDate() {
        return this.exposureDate;
    }

    public AdditionalInformation exposureDate(LocalDate exposureDate) {
        this.setExposureDate(exposureDate);
        return this;
    }

    public void setExposureDate(LocalDate exposureDate) {
        this.exposureDate = exposureDate;
    }

    public TypeToilet getTypeToilet() {
        return this.typeToilet;
    }

    public AdditionalInformation typeToilet(TypeToilet typeToilet) {
        this.setTypeToilet(typeToilet);
        return this;
    }

    public void setTypeToilet(TypeToilet typeToilet) {
        this.typeToilet = typeToilet;
    }

    public String getEnvironmentalRisk() {
        return this.environmentalRisk;
    }

    public AdditionalInformation environmentalRisk(String environmentalRisk) {
        this.setEnvironmentalRisk(environmentalRisk);
        return this;
    }

    public void setEnvironmentalRisk(String environmentalRisk) {
        this.environmentalRisk = environmentalRisk;
    }

    public Boolean getCardiovascularDisease() {
        return this.cardiovascularDisease;
    }

    public AdditionalInformation cardiovascularDisease(Boolean cardiovascularDisease) {
        this.setCardiovascularDisease(cardiovascularDisease);
        return this;
    }

    public void setCardiovascularDisease(Boolean cardiovascularDisease) {
        this.cardiovascularDisease = cardiovascularDisease;
    }

    public Boolean getHypertension() {
        return this.hypertension;
    }

    public AdditionalInformation hypertension(Boolean hypertension) {
        this.setHypertension(hypertension);
        return this;
    }

    public void setHypertension(Boolean hypertension) {
        this.hypertension = hypertension;
    }

    public Boolean getDiabetes() {
        return this.diabetes;
    }

    public AdditionalInformation diabetes(Boolean diabetes) {
        this.setDiabetes(diabetes);
        return this;
    }

    public void setDiabetes(Boolean diabetes) {
        this.diabetes = diabetes;
    }

    public Boolean getStomachDisease() {
        return this.stomachDisease;
    }

    public AdditionalInformation stomachDisease(Boolean stomachDisease) {
        this.setStomachDisease(stomachDisease);
        return this;
    }

    public void setStomachDisease(Boolean stomachDisease) {
        this.stomachDisease = stomachDisease;
    }

    public Boolean getChronicLungDisease() {
        return this.chronicLungDisease;
    }

    public AdditionalInformation chronicLungDisease(Boolean chronicLungDisease) {
        this.setChronicLungDisease(chronicLungDisease);
        return this;
    }

    public void setChronicLungDisease(Boolean chronicLungDisease) {
        this.chronicLungDisease = chronicLungDisease;
    }

    public Boolean getAsthma() {
        return this.asthma;
    }

    public AdditionalInformation asthma(Boolean asthma) {
        this.setAsthma(asthma);
        return this;
    }

    public void setAsthma(Boolean asthma) {
        this.asthma = asthma;
    }

    public Boolean getGoiter() {
        return this.goiter;
    }

    public AdditionalInformation goiter(Boolean goiter) {
        this.setGoiter(goiter);
        return this;
    }

    public void setGoiter(Boolean goiter) {
        this.goiter = goiter;
    }

    public Boolean getHepatitis() {
        return this.hepatitis;
    }

    public AdditionalInformation hepatitis(Boolean hepatitis) {
        this.setHepatitis(hepatitis);
        return this;
    }

    public void setHepatitis(Boolean hepatitis) {
        this.hepatitis = hepatitis;
    }

    public Boolean getCongenitalHeartDisease() {
        return this.congenitalHeartDisease;
    }

    public AdditionalInformation congenitalHeartDisease(Boolean congenitalHeartDisease) {
        this.setCongenitalHeartDisease(congenitalHeartDisease);
        return this;
    }

    public void setCongenitalHeartDisease(Boolean congenitalHeartDisease) {
        this.congenitalHeartDisease = congenitalHeartDisease;
    }

    public Boolean getMentalDisorders() {
        return this.mentalDisorders;
    }

    public AdditionalInformation mentalDisorders(Boolean mentalDisorders) {
        this.setMentalDisorders(mentalDisorders);
        return this;
    }

    public void setMentalDisorders(Boolean mentalDisorders) {
        this.mentalDisorders = mentalDisorders;
    }

    public Boolean getAutism() {
        return this.autism;
    }

    public AdditionalInformation autism(Boolean autism) {
        this.setAutism(autism);
        return this;
    }

    public void setAutism(Boolean autism) {
        this.autism = autism;
    }

    public Boolean getEpilepsy() {
        return this.epilepsy;
    }

    public AdditionalInformation epilepsy(Boolean epilepsy) {
        this.setEpilepsy(epilepsy);
        return this;
    }

    public void setEpilepsy(Boolean epilepsy) {
        this.epilepsy = epilepsy;
    }

    public String getCancer() {
        return this.cancer;
    }

    public AdditionalInformation cancer(String cancer) {
        this.setCancer(cancer);
        return this;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public String getTuberculosis() {
        return this.tuberculosis;
    }

    public AdditionalInformation tuberculosis(String tuberculosis) {
        this.setTuberculosis(tuberculosis);
        return this;
    }

    public void setTuberculosis(String tuberculosis) {
        this.tuberculosis = tuberculosis;
    }

    public String getOtherDiseases() {
        return this.otherDiseases;
    }

    public AdditionalInformation otherDiseases(String otherDiseases) {
        this.setOtherDiseases(otherDiseases);
        return this;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public String getContraceptiveMethod() {
        return this.contraceptiveMethod;
    }

    public AdditionalInformation contraceptiveMethod(String contraceptiveMethod) {
        this.setContraceptiveMethod(contraceptiveMethod);
        return this;
    }

    public void setContraceptiveMethod(String contraceptiveMethod) {
        this.contraceptiveMethod = contraceptiveMethod;
    }

    public Integer getLastPregnancy() {
        return this.lastPregnancy;
    }

    public AdditionalInformation lastPregnancy(Integer lastPregnancy) {
        this.setLastPregnancy(lastPregnancy);
        return this;
    }

    public void setLastPregnancy(Integer lastPregnancy) {
        this.lastPregnancy = lastPregnancy;
    }

    public Integer getNumberOfPregnancies() {
        return this.numberOfPregnancies;
    }

    public AdditionalInformation numberOfPregnancies(Integer numberOfPregnancies) {
        this.setNumberOfPregnancies(numberOfPregnancies);
        return this;
    }

    public void setNumberOfPregnancies(Integer numberOfPregnancies) {
        this.numberOfPregnancies = numberOfPregnancies;
    }

    public Integer getNumberOfMiscarriages() {
        return this.numberOfMiscarriages;
    }

    public AdditionalInformation numberOfMiscarriages(Integer numberOfMiscarriages) {
        this.setNumberOfMiscarriages(numberOfMiscarriages);
        return this;
    }

    public void setNumberOfMiscarriages(Integer numberOfMiscarriages) {
        this.numberOfMiscarriages = numberOfMiscarriages;
    }

    public Integer getNumberOfAbortions() {
        return this.numberOfAbortions;
    }

    public AdditionalInformation numberOfAbortions(Integer numberOfAbortions) {
        this.setNumberOfAbortions(numberOfAbortions);
        return this;
    }

    public void setNumberOfAbortions(Integer numberOfAbortions) {
        this.numberOfAbortions = numberOfAbortions;
    }

    public Integer getNumberOfBirths() {
        return this.numberOfBirths;
    }

    public AdditionalInformation numberOfBirths(Integer numberOfBirths) {
        this.setNumberOfBirths(numberOfBirths);
        return this;
    }

    public void setNumberOfBirths(Integer numberOfBirths) {
        this.numberOfBirths = numberOfBirths;
    }

    public Integer getVaginalDelivery() {
        return this.vaginalDelivery;
    }

    public AdditionalInformation vaginalDelivery(Integer vaginalDelivery) {
        this.setVaginalDelivery(vaginalDelivery);
        return this;
    }

    public void setVaginalDelivery(Integer vaginalDelivery) {
        this.vaginalDelivery = vaginalDelivery;
    }

    public Integer getCesareanSection() {
        return this.cesareanSection;
    }

    public AdditionalInformation cesareanSection(Integer cesareanSection) {
        this.setCesareanSection(cesareanSection);
        return this;
    }

    public void setCesareanSection(Integer cesareanSection) {
        this.cesareanSection = cesareanSection;
    }

    public Integer getDifficultDelivery() {
        return this.difficultDelivery;
    }

    public AdditionalInformation difficultDelivery(Integer difficultDelivery) {
        this.setDifficultDelivery(difficultDelivery);
        return this;
    }

    public void setDifficultDelivery(Integer difficultDelivery) {
        this.difficultDelivery = difficultDelivery;
    }

    public Integer getNumberOfFullTermBirths() {
        return this.numberOfFullTermBirths;
    }

    public AdditionalInformation numberOfFullTermBirths(Integer numberOfFullTermBirths) {
        this.setNumberOfFullTermBirths(numberOfFullTermBirths);
        return this;
    }

    public void setNumberOfFullTermBirths(Integer numberOfFullTermBirths) {
        this.numberOfFullTermBirths = numberOfFullTermBirths;
    }

    public Integer getNumberOfPrematureBirths() {
        return this.numberOfPrematureBirths;
    }

    public AdditionalInformation numberOfPrematureBirths(Integer numberOfPrematureBirths) {
        this.setNumberOfPrematureBirths(numberOfPrematureBirths);
        return this;
    }

    public void setNumberOfPrematureBirths(Integer numberOfPrematureBirths) {
        this.numberOfPrematureBirths = numberOfPrematureBirths;
    }

    public Integer getNumberOfChildrenAlive() {
        return this.numberOfChildrenAlive;
    }

    public AdditionalInformation numberOfChildrenAlive(Integer numberOfChildrenAlive) {
        this.setNumberOfChildrenAlive(numberOfChildrenAlive);
        return this;
    }

    public void setNumberOfChildrenAlive(Integer numberOfChildrenAlive) {
        this.numberOfChildrenAlive = numberOfChildrenAlive;
    }

    public String getGynecologicalDiseases() {
        return this.gynecologicalDiseases;
    }

    public AdditionalInformation gynecologicalDiseases(String gynecologicalDiseases) {
        this.setGynecologicalDiseases(gynecologicalDiseases);
        return this;
    }

    public void setGynecologicalDiseases(String gynecologicalDiseases) {
        this.gynecologicalDiseases = gynecologicalDiseases;
    }

    public BirthStatusType getBirthStatus() {
        return this.birthStatus;
    }

    public AdditionalInformation birthStatus(BirthStatusType birthStatus) {
        this.setBirthStatus(birthStatus);
        return this;
    }

    public void setBirthStatus(BirthStatusType birthStatus) {
        this.birthStatus = birthStatus;
    }

    public Double getBirthWeight() {
        return this.birthWeight;
    }

    public AdditionalInformation birthWeight(Double birthWeight) {
        this.setBirthWeight(birthWeight);
        return this;
    }

    public void setBirthWeight(Double birthWeight) {
        this.birthWeight = birthWeight;
    }

    public Double getBirthHeight() {
        return this.birthHeight;
    }

    public AdditionalInformation birthHeight(Double birthHeight) {
        this.setBirthHeight(birthHeight);
        return this;
    }

    public void setBirthHeight(Double birthHeight) {
        this.birthHeight = birthHeight;
    }

    public String getBirthDefectNote() {
        return this.birthDefectNote;
    }

    public AdditionalInformation birthDefectNote(String birthDefectNote) {
        this.setBirthDefectNote(birthDefectNote);
        return this;
    }

    public void setBirthDefectNote(String birthDefectNote) {
        this.birthDefectNote = birthDefectNote;
    }

    public String getOtherBirthNote() {
        return this.otherBirthNote;
    }

    public AdditionalInformation otherBirthNote(String otherBirthNote) {
        this.setOtherBirthNote(otherBirthNote);
        return this;
    }

    public void setOtherBirthNote(String otherBirthNote) {
        this.otherBirthNote = otherBirthNote;
    }

    public String getOtherHealthNote() {
        return this.otherHealthNote;
    }

    public AdditionalInformation otherHealthNote(String otherHealthNote) {
        this.setOtherHealthNote(otherHealthNote);
        return this;
    }

    public void setOtherHealthNote(String otherHealthNote) {
        this.otherHealthNote = otherHealthNote;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        if (this.patient != null) {
            this.patient.setAdditionalInfo(null);
        }
        if (patient != null) {
            patient.setAdditionalInfo(this);
        }
        this.patient = patient;
    }

    public AdditionalInformation patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalInformation)) {
            return false;
        }
        return getId() != null && getId().equals(((AdditionalInformation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditionalInformation{" +
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
