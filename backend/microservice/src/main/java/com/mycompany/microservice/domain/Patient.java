package com.mycompany.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.microservice.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "blood_type_abo")
    private String bloodTypeAbo;

    @Column(name = "blood_type_rh")
    private String bloodTypeRh;

    @Column(name = "ethnic")
    private String ethnic;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "religion")
    private String religion;

    @Column(name = "job")
    private String job;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "id_issue_date")
    private LocalDate idIssueDate;

    @Column(name = "id_issue_place")
    private String idIssuePlace;

    @Column(name = "health_insurance_number")
    private String healthInsuranceNumber;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "permanent_ward")
    private String permanentWard;

    @Column(name = "permanent_district")
    private String permanentDistrict;

    @Column(name = "permanent_province")
    private String permanentProvince;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "current_ward")
    private String currentWard;

    @Column(name = "current_district")
    private String currentDistrict;

    @Column(name = "current_province")
    private String currentProvince;

    @Column(name = "landline_phone")
    private String landlinePhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "email")
    private String email;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "caregiver_name")
    private String caregiverName;

    @Column(name = "caregiver_relation")
    private String caregiverRelation;

    @Column(name = "caregiver_landline_phone")
    private String caregiverLandlinePhone;

    @Column(name = "caregiver_mobile_phone")
    private String caregiverMobilePhone;

    @Column(name = "family_code")
    private String familyCode;

    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AdditionalInformation additionalInfo;

    @JsonIgnoreProperties(value = { "vaccines", "patient" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private VaccinationForBaby vaccinationsForBaby;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Allergy> allergies = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<Disability> disabilities = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<SurgeryHistory> surgeryHistories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<FamilyAllergy> familyAllergies = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "diseases", "patient" }, allowSetters = true)
    private Set<FamilyDisease> familyDiseases = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<VaccinationTCMR> vaccinationsTCMRS = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    private Set<PregnancyTetanus> pregnancyTetanuses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    @JsonIgnoreProperties(value = { "organExamination", "paraclinicalResults", "patient" }, allowSetters = true)
    private Set<MedicalRecord> medicalRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Patient fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Patient gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Patient dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public Patient placeOfBirth(String placeOfBirth) {
        this.setPlaceOfBirth(placeOfBirth);
        return this;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getBloodTypeAbo() {
        return this.bloodTypeAbo;
    }

    public Patient bloodTypeAbo(String bloodTypeAbo) {
        this.setBloodTypeAbo(bloodTypeAbo);
        return this;
    }

    public void setBloodTypeAbo(String bloodTypeAbo) {
        this.bloodTypeAbo = bloodTypeAbo;
    }

    public String getBloodTypeRh() {
        return this.bloodTypeRh;
    }

    public Patient bloodTypeRh(String bloodTypeRh) {
        this.setBloodTypeRh(bloodTypeRh);
        return this;
    }

    public void setBloodTypeRh(String bloodTypeRh) {
        this.bloodTypeRh = bloodTypeRh;
    }

    public String getEthnic() {
        return this.ethnic;
    }

    public Patient ethnic(String ethnic) {
        this.setEthnic(ethnic);
        return this;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Patient nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return this.religion;
    }

    public Patient religion(String religion) {
        this.setReligion(religion);
        return this;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getJob() {
        return this.job;
    }

    public Patient job(String job) {
        this.setJob(job);
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Patient idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getIdIssueDate() {
        return this.idIssueDate;
    }

    public Patient idIssueDate(LocalDate idIssueDate) {
        this.setIdIssueDate(idIssueDate);
        return this;
    }

    public void setIdIssueDate(LocalDate idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getIdIssuePlace() {
        return this.idIssuePlace;
    }

    public Patient idIssuePlace(String idIssuePlace) {
        this.setIdIssuePlace(idIssuePlace);
        return this;
    }

    public void setIdIssuePlace(String idIssuePlace) {
        this.idIssuePlace = idIssuePlace;
    }

    public String getHealthInsuranceNumber() {
        return this.healthInsuranceNumber;
    }

    public Patient healthInsuranceNumber(String healthInsuranceNumber) {
        this.setHealthInsuranceNumber(healthInsuranceNumber);
        return this;
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public Patient permanentAddress(String permanentAddress) {
        this.setPermanentAddress(permanentAddress);
        return this;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentWard() {
        return this.permanentWard;
    }

    public Patient permanentWard(String permanentWard) {
        this.setPermanentWard(permanentWard);
        return this;
    }

    public void setPermanentWard(String permanentWard) {
        this.permanentWard = permanentWard;
    }

    public String getPermanentDistrict() {
        return this.permanentDistrict;
    }

    public Patient permanentDistrict(String permanentDistrict) {
        this.setPermanentDistrict(permanentDistrict);
        return this;
    }

    public void setPermanentDistrict(String permanentDistrict) {
        this.permanentDistrict = permanentDistrict;
    }

    public String getPermanentProvince() {
        return this.permanentProvince;
    }

    public Patient permanentProvince(String permanentProvince) {
        this.setPermanentProvince(permanentProvince);
        return this;
    }

    public void setPermanentProvince(String permanentProvince) {
        this.permanentProvince = permanentProvince;
    }

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    public Patient currentAddress(String currentAddress) {
        this.setCurrentAddress(currentAddress);
        return this;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentWard() {
        return this.currentWard;
    }

    public Patient currentWard(String currentWard) {
        this.setCurrentWard(currentWard);
        return this;
    }

    public void setCurrentWard(String currentWard) {
        this.currentWard = currentWard;
    }

    public String getCurrentDistrict() {
        return this.currentDistrict;
    }

    public Patient currentDistrict(String currentDistrict) {
        this.setCurrentDistrict(currentDistrict);
        return this;
    }

    public void setCurrentDistrict(String currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public String getCurrentProvince() {
        return this.currentProvince;
    }

    public Patient currentProvince(String currentProvince) {
        this.setCurrentProvince(currentProvince);
        return this;
    }

    public void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    public String getLandlinePhone() {
        return this.landlinePhone;
    }

    public Patient landlinePhone(String landlinePhone) {
        this.setLandlinePhone(landlinePhone);
        return this;
    }

    public void setLandlinePhone(String landlinePhone) {
        this.landlinePhone = landlinePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public Patient mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return this.email;
    }

    public Patient email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotherName() {
        return this.motherName;
    }

    public Patient motherName(String motherName) {
        this.setMotherName(motherName);
        return this;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public Patient fatherName(String fatherName) {
        this.setFatherName(fatherName);
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCaregiverName() {
        return this.caregiverName;
    }

    public Patient caregiverName(String caregiverName) {
        this.setCaregiverName(caregiverName);
        return this;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public String getCaregiverRelation() {
        return this.caregiverRelation;
    }

    public Patient caregiverRelation(String caregiverRelation) {
        this.setCaregiverRelation(caregiverRelation);
        return this;
    }

    public void setCaregiverRelation(String caregiverRelation) {
        this.caregiverRelation = caregiverRelation;
    }

    public String getCaregiverLandlinePhone() {
        return this.caregiverLandlinePhone;
    }

    public Patient caregiverLandlinePhone(String caregiverLandlinePhone) {
        this.setCaregiverLandlinePhone(caregiverLandlinePhone);
        return this;
    }

    public void setCaregiverLandlinePhone(String caregiverLandlinePhone) {
        this.caregiverLandlinePhone = caregiverLandlinePhone;
    }

    public String getCaregiverMobilePhone() {
        return this.caregiverMobilePhone;
    }

    public Patient caregiverMobilePhone(String caregiverMobilePhone) {
        this.setCaregiverMobilePhone(caregiverMobilePhone);
        return this;
    }

    public void setCaregiverMobilePhone(String caregiverMobilePhone) {
        this.caregiverMobilePhone = caregiverMobilePhone;
    }

    public String getFamilyCode() {
        return this.familyCode;
    }

    public Patient familyCode(String familyCode) {
        this.setFamilyCode(familyCode);
        return this;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public AdditionalInformation getAdditionalInfo() {
        return this.additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInformation additionalInformation) {
        this.additionalInfo = additionalInformation;
    }

    public Patient additionalInfo(AdditionalInformation additionalInformation) {
        this.setAdditionalInfo(additionalInformation);
        return this;
    }

    public VaccinationForBaby getVaccinationsForBaby() {
        return this.vaccinationsForBaby;
    }

    public void setVaccinationsForBaby(VaccinationForBaby vaccinationForBaby) {
        this.vaccinationsForBaby = vaccinationForBaby;
    }

    public Patient vaccinationsForBaby(VaccinationForBaby vaccinationForBaby) {
        this.setVaccinationsForBaby(vaccinationForBaby);
        return this;
    }

    public Set<Allergy> getAllergies() {
        return this.allergies;
    }

    public void setAllergies(Set<Allergy> allergies) {
        if (this.allergies != null) {
            this.allergies.forEach(i -> i.setPatient(null));
        }
        if (allergies != null) {
            allergies.forEach(i -> i.setPatient(this));
        }
        this.allergies = allergies;
    }

    public Patient allergies(Set<Allergy> allergies) {
        this.setAllergies(allergies);
        return this;
    }

    public Patient addAllergies(Allergy allergy) {
        this.allergies.add(allergy);
        allergy.setPatient(this);
        return this;
    }

    public Patient removeAllergies(Allergy allergy) {
        this.allergies.remove(allergy);
        allergy.setPatient(null);
        return this;
    }

    public Set<Disability> getDisabilities() {
        return this.disabilities;
    }

    public void setDisabilities(Set<Disability> disabilities) {
        if (this.disabilities != null) {
            this.disabilities.forEach(i -> i.setPatient(null));
        }
        if (disabilities != null) {
            disabilities.forEach(i -> i.setPatient(this));
        }
        this.disabilities = disabilities;
    }

    public Patient disabilities(Set<Disability> disabilities) {
        this.setDisabilities(disabilities);
        return this;
    }

    public Patient addDisabilities(Disability disability) {
        this.disabilities.add(disability);
        disability.setPatient(this);
        return this;
    }

    public Patient removeDisabilities(Disability disability) {
        this.disabilities.remove(disability);
        disability.setPatient(null);
        return this;
    }

    public Set<SurgeryHistory> getSurgeryHistories() {
        return this.surgeryHistories;
    }

    public void setSurgeryHistories(Set<SurgeryHistory> surgeryHistories) {
        if (this.surgeryHistories != null) {
            this.surgeryHistories.forEach(i -> i.setPatient(null));
        }
        if (surgeryHistories != null) {
            surgeryHistories.forEach(i -> i.setPatient(this));
        }
        this.surgeryHistories = surgeryHistories;
    }

    public Patient surgeryHistories(Set<SurgeryHistory> surgeryHistories) {
        this.setSurgeryHistories(surgeryHistories);
        return this;
    }

    public Patient addSurgeryHistories(SurgeryHistory surgeryHistory) {
        this.surgeryHistories.add(surgeryHistory);
        surgeryHistory.setPatient(this);
        return this;
    }

    public Patient removeSurgeryHistories(SurgeryHistory surgeryHistory) {
        this.surgeryHistories.remove(surgeryHistory);
        surgeryHistory.setPatient(null);
        return this;
    }

    public Set<FamilyAllergy> getFamilyAllergies() {
        return this.familyAllergies;
    }

    public void setFamilyAllergies(Set<FamilyAllergy> familyAllergies) {
        if (this.familyAllergies != null) {
            this.familyAllergies.forEach(i -> i.setPatient(null));
        }
        if (familyAllergies != null) {
            familyAllergies.forEach(i -> i.setPatient(this));
        }
        this.familyAllergies = familyAllergies;
    }

    public Patient familyAllergies(Set<FamilyAllergy> familyAllergies) {
        this.setFamilyAllergies(familyAllergies);
        return this;
    }

    public Patient addFamilyAllergies(FamilyAllergy familyAllergy) {
        this.familyAllergies.add(familyAllergy);
        familyAllergy.setPatient(this);
        return this;
    }

    public Patient removeFamilyAllergies(FamilyAllergy familyAllergy) {
        this.familyAllergies.remove(familyAllergy);
        familyAllergy.setPatient(null);
        return this;
    }

    public Set<FamilyDisease> getFamilyDiseases() {
        return this.familyDiseases;
    }

    public void setFamilyDiseases(Set<FamilyDisease> familyDiseases) {
        if (this.familyDiseases != null) {
            this.familyDiseases.forEach(i -> i.setPatient(null));
        }
        if (familyDiseases != null) {
            familyDiseases.forEach(i -> i.setPatient(this));
        }
        this.familyDiseases = familyDiseases;
    }

    public Patient familyDiseases(Set<FamilyDisease> familyDiseases) {
        this.setFamilyDiseases(familyDiseases);
        return this;
    }

    public Patient addFamilyDiseases(FamilyDisease familyDisease) {
        this.familyDiseases.add(familyDisease);
        familyDisease.setPatient(this);
        return this;
    }

    public Patient removeFamilyDiseases(FamilyDisease familyDisease) {
        this.familyDiseases.remove(familyDisease);
        familyDisease.setPatient(null);
        return this;
    }

    public Set<VaccinationTCMR> getVaccinationsTCMRS() {
        return this.vaccinationsTCMRS;
    }

    public void setVaccinationsTCMRS(Set<VaccinationTCMR> vaccinationTCMRS) {
        if (this.vaccinationsTCMRS != null) {
            this.vaccinationsTCMRS.forEach(i -> i.setPatient(null));
        }
        if (vaccinationTCMRS != null) {
            vaccinationTCMRS.forEach(i -> i.setPatient(this));
        }
        this.vaccinationsTCMRS = vaccinationTCMRS;
    }

    public Patient vaccinationsTCMRS(Set<VaccinationTCMR> vaccinationTCMRS) {
        this.setVaccinationsTCMRS(vaccinationTCMRS);
        return this;
    }

    public Patient addVaccinationsTCMR(VaccinationTCMR vaccinationTCMR) {
        this.vaccinationsTCMRS.add(vaccinationTCMR);
        vaccinationTCMR.setPatient(this);
        return this;
    }

    public Patient removeVaccinationsTCMR(VaccinationTCMR vaccinationTCMR) {
        this.vaccinationsTCMRS.remove(vaccinationTCMR);
        vaccinationTCMR.setPatient(null);
        return this;
    }

    public Set<PregnancyTetanus> getPregnancyTetanuses() {
        return this.pregnancyTetanuses;
    }

    public void setPregnancyTetanuses(Set<PregnancyTetanus> pregnancyTetanuses) {
        if (this.pregnancyTetanuses != null) {
            this.pregnancyTetanuses.forEach(i -> i.setPatient(null));
        }
        if (pregnancyTetanuses != null) {
            pregnancyTetanuses.forEach(i -> i.setPatient(this));
        }
        this.pregnancyTetanuses = pregnancyTetanuses;
    }

    public Patient pregnancyTetanuses(Set<PregnancyTetanus> pregnancyTetanuses) {
        this.setPregnancyTetanuses(pregnancyTetanuses);
        return this;
    }

    public Patient addPregnancyTetanus(PregnancyTetanus pregnancyTetanus) {
        this.pregnancyTetanuses.add(pregnancyTetanus);
        pregnancyTetanus.setPatient(this);
        return this;
    }

    public Patient removePregnancyTetanus(PregnancyTetanus pregnancyTetanus) {
        this.pregnancyTetanuses.remove(pregnancyTetanus);
        pregnancyTetanus.setPatient(null);
        return this;
    }

    public Set<MedicalRecord> getMedicalRecords() {
        return this.medicalRecords;
    }

    public void setMedicalRecords(Set<MedicalRecord> medicalRecords) {
        if (this.medicalRecords != null) {
            this.medicalRecords.forEach(i -> i.setPatient(null));
        }
        if (medicalRecords != null) {
            medicalRecords.forEach(i -> i.setPatient(this));
        }
        this.medicalRecords = medicalRecords;
    }

    public Patient medicalRecords(Set<MedicalRecord> medicalRecords) {
        this.setMedicalRecords(medicalRecords);
        return this;
    }

    public Patient addMedicalRecords(MedicalRecord medicalRecord) {
        this.medicalRecords.add(medicalRecord);
        medicalRecord.setPatient(this);
        return this;
    }

    public Patient removeMedicalRecords(MedicalRecord medicalRecord) {
        this.medicalRecords.remove(medicalRecord);
        medicalRecord.setPatient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return getId() != null && getId().equals(((Patient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", gender='" + getGender() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", bloodTypeAbo='" + getBloodTypeAbo() + "'" +
            ", bloodTypeRh='" + getBloodTypeRh() + "'" +
            ", ethnic='" + getEthnic() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", religion='" + getReligion() + "'" +
            ", job='" + getJob() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", idIssueDate='" + getIdIssueDate() + "'" +
            ", idIssuePlace='" + getIdIssuePlace() + "'" +
            ", healthInsuranceNumber='" + getHealthInsuranceNumber() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", permanentWard='" + getPermanentWard() + "'" +
            ", permanentDistrict='" + getPermanentDistrict() + "'" +
            ", permanentProvince='" + getPermanentProvince() + "'" +
            ", currentAddress='" + getCurrentAddress() + "'" +
            ", currentWard='" + getCurrentWard() + "'" +
            ", currentDistrict='" + getCurrentDistrict() + "'" +
            ", currentProvince='" + getCurrentProvince() + "'" +
            ", landlinePhone='" + getLandlinePhone() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", caregiverName='" + getCaregiverName() + "'" +
            ", caregiverRelation='" + getCaregiverRelation() + "'" +
            ", caregiverLandlinePhone='" + getCaregiverLandlinePhone() + "'" +
            ", caregiverMobilePhone='" + getCaregiverMobilePhone() + "'" +
            ", familyCode='" + getFamilyCode() + "'" +
            "}";
    }
}
