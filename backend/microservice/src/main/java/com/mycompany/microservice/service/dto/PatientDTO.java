package com.mycompany.microservice.service.dto;

import com.mycompany.microservice.domain.enumeration.Gender;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.microservice.domain.Patient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private Gender gender;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String bloodTypeAbo;

    private String bloodTypeRh;

    private String ethnic;

    private String nationality;

    private String religion;

    private String job;

    private String idNumber;

    private LocalDate idIssueDate;

    private String idIssuePlace;

    private String healthInsuranceNumber;

    private String permanentAddress;

    private String permanentWard;

    private String permanentDistrict;

    private String permanentProvince;

    private String currentAddress;

    private String currentWard;

    private String currentDistrict;

    private String currentProvince;

    private String landlinePhone;

    private String mobilePhone;

    private String email;

    private String motherName;

    private String fatherName;

    private String caregiverName;

    private String caregiverRelation;

    private String caregiverLandlinePhone;

    private String caregiverMobilePhone;

    private String familyCode;

    private AdditionalInformationDTO additionalInfo;

    private VaccinationForBabyDTO vaccinationsForBaby;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getBloodTypeAbo() {
        return bloodTypeAbo;
    }

    public void setBloodTypeAbo(String bloodTypeAbo) {
        this.bloodTypeAbo = bloodTypeAbo;
    }

    public String getBloodTypeRh() {
        return bloodTypeRh;
    }

    public void setBloodTypeRh(String bloodTypeRh) {
        this.bloodTypeRh = bloodTypeRh;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getIdIssueDate() {
        return idIssueDate;
    }

    public void setIdIssueDate(LocalDate idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getIdIssuePlace() {
        return idIssuePlace;
    }

    public void setIdIssuePlace(String idIssuePlace) {
        this.idIssuePlace = idIssuePlace;
    }

    public String getHealthInsuranceNumber() {
        return healthInsuranceNumber;
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentWard() {
        return permanentWard;
    }

    public void setPermanentWard(String permanentWard) {
        this.permanentWard = permanentWard;
    }

    public String getPermanentDistrict() {
        return permanentDistrict;
    }

    public void setPermanentDistrict(String permanentDistrict) {
        this.permanentDistrict = permanentDistrict;
    }

    public String getPermanentProvince() {
        return permanentProvince;
    }

    public void setPermanentProvince(String permanentProvince) {
        this.permanentProvince = permanentProvince;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentWard() {
        return currentWard;
    }

    public void setCurrentWard(String currentWard) {
        this.currentWard = currentWard;
    }

    public String getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(String currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public String getCurrentProvince() {
        return currentProvince;
    }

    public void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    public String getLandlinePhone() {
        return landlinePhone;
    }

    public void setLandlinePhone(String landlinePhone) {
        this.landlinePhone = landlinePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public String getCaregiverRelation() {
        return caregiverRelation;
    }

    public void setCaregiverRelation(String caregiverRelation) {
        this.caregiverRelation = caregiverRelation;
    }

    public String getCaregiverLandlinePhone() {
        return caregiverLandlinePhone;
    }

    public void setCaregiverLandlinePhone(String caregiverLandlinePhone) {
        this.caregiverLandlinePhone = caregiverLandlinePhone;
    }

    public String getCaregiverMobilePhone() {
        return caregiverMobilePhone;
    }

    public void setCaregiverMobilePhone(String caregiverMobilePhone) {
        this.caregiverMobilePhone = caregiverMobilePhone;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public AdditionalInformationDTO getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInformationDTO additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public VaccinationForBabyDTO getVaccinationsForBaby() {
        return vaccinationsForBaby;
    }

    public void setVaccinationsForBaby(VaccinationForBabyDTO vaccinationsForBaby) {
        this.vaccinationsForBaby = vaccinationsForBaby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDTO)) {
            return false;
        }

        PatientDTO patientDTO = (PatientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDTO{" +
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
            ", additionalInfo=" + getAdditionalInfo() +
            ", vaccinationsForBaby=" + getVaccinationsForBaby() +
            "}";
    }
}
